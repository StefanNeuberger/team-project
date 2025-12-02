import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import type { FieldValues } from "react-hook-form";

type ItemFormProps<F extends FieldValues> = {
    form: F,
}

export default function ItemForm<F extends FieldValues>( { form }: ItemFormProps<F> ) {

    const disableSubmitButton = form.formState.isSubmitting || !form.formState.isDirty || !form.formState.isValid;

    return (
        <div className={ "space-y-6 my-4" }>
            <FormField
                control={ form.control }
                name={ "name" }
                render={
                    ( { field } ) => (
                        <FormItem className={ "gap-4" }>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Item Name</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input placeholder={ "Type in name..." } { ...field } />
                            </FormControl>
                            <FormDescription>
                                Field is required. Minimum 2 characters.
                            </FormDescription>
                        </FormItem>
                    )
                }/>
            <FormField
                control={ form.control }
                name={ "sku" }
                render={
                    ( { field } ) => (
                        <FormItem className={ "gap-4" }>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>SKU</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input placeholder={ "Type in SKU e.g. KL-MED-15..." } { ...field } />
                            </FormControl>
                            <FormDescription>
                                Field is required.
                            </FormDescription>
                        </FormItem>
                    )
                }/>
            <Button disabled={ disableSubmitButton } type={ "submit" }>Submit</Button>
        </div>
    )
}
