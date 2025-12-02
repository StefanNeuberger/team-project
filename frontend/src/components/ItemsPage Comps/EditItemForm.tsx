import { useForm } from "react-hook-form";
import { Form } from "@/components/ui/form.tsx";
import { zodResolver } from "@hookform/resolvers/zod";
import { type ItemFormData, itemFormSchema } from "@/types/items.ts";
import ItemForm from "@/components/ItemsPage Comps/ItemForm.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { getGetAllItemsQueryKey, useUpdateItemById } from "@/api/generated/items/items.ts";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";

type EditItemFormProps = {
    item: Item
    closeEdit: () => void
}

export default function EditItemForm( { item, closeEdit }: EditItemFormProps ) {

    const editItem = useUpdateItemById();

    const queryClient = useQueryClient();

    const form = useForm<ItemFormData>( {
        resolver: zodResolver( itemFormSchema ),
        defaultValues: {
            name: item.name,
            sku: item.sku || ""
        }
    } );

    const handleEditItem = ( data: ItemFormData ) => {
        if ( !form.formState.isDirty || !form.formState.isValid ) {
            return;
        }

        editItem.mutate(
            {
                id: item.id,
                data: {
                    name: data.name,
                    sku: data.sku
                }
            },
            {
                onSuccess: () => {
                    console.log( "Item updated successfully." );
                    toast.success( "Item updated successfully." );
                    queryClient.invalidateQueries( { queryKey: getGetAllItemsQueryKey() } );
                    closeEdit();
                },
                onError: ( error ) => {
                    console.error( "Failed to update item:" );
                    toast.error( error.response?.data.message || error.message || "Failed to update item." );
                }
            }
        )
    };

    return (
        <Form { ...form }>
            <form onSubmit={ form.handleSubmit( handleEditItem ) }>
                <ItemForm form={ form }/>
            </form>
        </Form>
    )
}
