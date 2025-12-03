import type { FieldValues } from "react-hook-form";
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Field, FieldDescription } from "@/components/ui/field.tsx";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover.tsx";
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList
} from "@/components/ui/command.tsx";
import { useGetAllItems } from "@/api/generated/items/items.ts";
import { useState } from "react";

type LineItemFormProps<F extends FieldValues> = {
    form: F,
}

export default function LineItemForm<F extends FieldValues>( { form }: Readonly<LineItemFormProps<F>> ) {

    const [ showItems, setShowItems ] = useState( false );
    const [ selectedItem, setSelectedItem ] = useState<string | null>( null );

    const { data: allItemsData } = useGetAllItems();

    const disableSubmitButton = form.formState.isSubmitting || !form.formState.isDirty;
    return (
        <div className={ "space-y-6 my-4" }>
            <FormField
                control={ form.control }
                name={ "itemId" }
                render={
                    ( { field } ) => (
                        <Field>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Select an Item</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <Popover open={ showItems } onOpenChange={ setShowItems }>
                                <PopoverTrigger asChild>
                                    <FormControl>
                                        <Input
                                            placeholder="Search items..."
                                            value={ selectedItem || "" }
                                            readOnly
                                            onClick={ () => setShowItems( true ) }
                                        />
                                    </FormControl>
                                </PopoverTrigger>
                                <PopoverContent className={ "w-full p-0" } align={ "start" }>
                                    <Command>
                                        <CommandInput placeholder={ "Search items..." }/>
                                        <CommandList>
                                            <CommandEmpty>
                                                No items found.
                                            </CommandEmpty>
                                            <CommandGroup>
                                                { allItemsData?.data.map( ( item ) => (
                                                    <CommandItem
                                                        key={ item.id }
                                                        onSelect={ () => {
                                                            setSelectedItem( item.name );
                                                            field.onChange( item.id );
                                                            setShowItems( false );
                                                        } }>
                                                        { item.name }
                                                        <span className={ "text-xs ml-auto text-muted-foreground" }>
                                                            { item.sku }
                                                        </span>
                                                    </CommandItem>
                                                ) ) }
                                            </CommandGroup>
                                        </CommandList>
                                    </Command>
                                </PopoverContent>
                            </Popover>
                            <FieldDescription>
                                Choose the warehouse for this shipment.
                            </FieldDescription>
                        </Field>
                    )
                }/>
            <FormField
                control={ form.control }
                name={ "expectedQuantity" }
                render={
                    ( { field } ) => (
                        <FormItem className={ "gap-4" }>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Expected Quantity</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input
                                    type="number"
                                    placeholder={ "0" }
                                    { ...field }
                                    onChange={ ( e ) => field.onChange( Number( e.target.value ) ) }
                                />
                            </FormControl>
                            <FormDescription>
                                Must be at least 0.
                            </FormDescription>
                        </FormItem>
                    )
                }/>
            <FormField
                control={ form.control }
                name={ "receivedQuantity" }
                render={
                    ( { field } ) => (
                        <FormItem className={ "gap-4" }>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Received Quantity</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input
                                    type="number"
                                    placeholder={ "0" }
                                    { ...field }
                                    onChange={ ( e ) => field.onChange( Number( e.target.value ) ) }
                                />
                            </FormControl>
                            <FormDescription>
                                Must be at least 0.
                            </FormDescription>
                        </FormItem>
                    )
                }/>
            <Button disabled={ disableSubmitButton } type={ "submit" }>Submit</Button>
        </div>
    )
}
