import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage
} from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { getGetAllItemsQueryKey, useCreateItem } from "@/api/generated/items/items.ts";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";


type CreateItemFormProps = {
    closeDialog: () => void;
}

type CreateItemFormData = {
    name: string;
    sku: string;
}

const itemFormSchema = z.object( {
    name: z.string()
        .min( 1, "Item name is required" )
        .min( 2, "Item name must be at last 2 characters" )
        .max( 100, "Item name must be less than 100 characters" )
        .trim(),
    sku: z.string()
        .min( 1, "SKU is required" )
        .regex( /^[A-Z]{2}-[A-Z]{3}-\d{2}$/, "SKU must follow format XX-XXX-XX (e.g., KL-MED-15)" )
        .trim(),
} );

export default function CreateItemForm( { closeDialog }: CreateItemFormProps ) {

    const createItem = useCreateItem();
    const queryClient = useQueryClient();

    const form = useForm<CreateItemFormData>( {
        resolver: zodResolver( itemFormSchema ),
        defaultValues: {
            name: "",
            sku: ""
        }
    } );

    const handleCreateItem = ( data: CreateItemFormData ) => {
        createItem.mutate(
            {
                data: {
                    name: data.name,
                    sku: data.sku
                }
            },
            {
                onSuccess: () => {
                    toast.success( "Item created successfully." );
                    queryClient.invalidateQueries( { queryKey: getGetAllItemsQueryKey() } );
                    closeDialog();
                },
                onError: ( error ) => {
                    console.error( error );
                    toast.error( error.response?.data.message || error.message || "Failed to create item." );
                }
            }
        )
    }

    return (
        <Form { ...form }>
            <form onSubmit={ form.handleSubmit( handleCreateItem ) } className={ "space-y-6 my-4" }>
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
                <Button type={ "submit" }>Submit</Button>
            </form>
        </Form>

    )
}
