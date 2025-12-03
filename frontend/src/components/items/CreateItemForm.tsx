import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form } from "@/components/ui/form.tsx";
import { getGetAllItemsQueryKey, useCreateItem } from "@/api/generated/items/items.ts";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import ItemForm from "@/components/items/ItemForm.tsx";
import { type ItemFormData, itemFormSchema } from "@/types/items.ts";


type CreateItemFormProps = {
    closeDialog: () => void;
}


export default function CreateItemForm( { closeDialog }: CreateItemFormProps ) {

    const createItem = useCreateItem();
    const queryClient = useQueryClient();

    const form = useForm<ItemFormData>( {
        resolver: zodResolver( itemFormSchema ),
        defaultValues: {
            name: "",
            sku: ""
        }
    } );

    const handleCreateItem = ( data: ItemFormData ) => {
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
            <form onSubmit={ form.handleSubmit( handleCreateItem ) }>
                <ItemForm form={ form }/>
            </form>
        </Form>

    )
}
