import { motion } from "framer-motion";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert.tsx";
import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { toast } from "sonner";
import { getGetAllItemsQueryKey, useDeleteItemById } from "@/api/generated/items/items.ts";
import { getGetAllInventoryQueryKey } from "@/api/generated/inventory/inventory.ts";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { useQueryClient } from "@tanstack/react-query";

type DeleteItemAlertProps = {
    item: Item,
    toggleShowConfirmDelete: () => void,
    closeDialog: () => void
}

export default function DeleteItemAlert( {
                                             closeDialog,
                                             item,
                                             toggleShowConfirmDelete
                                         }: DeleteItemAlertProps ) {

    const { mutate: deleteItem, isPending } = useDeleteItemById();

    const queryClient = useQueryClient();

    const handleDeleteItem = () => {
        if ( !item.id ) {
            return;
        }
        deleteItem(
            { id: item.id },
            {
                onSuccess: () => {
                    toast.success( "Item deleted successfully." );

                    queryClient.invalidateQueries(
                        { queryKey: getGetAllItemsQueryKey() }
                    );

                    queryClient.invalidateQueries(
                        { queryKey: getGetAllInventoryQueryKey() }
                    );

                    closeDialog();
                },
                onError: ( error ) => {
                    toast.error( error.response?.data.message || error.message || "Failed to delete item." );
                    console.error( "Failed to delete item:", error );
                }
            }
        );
    }

    return (

        <motion.div
            initial={ { opacity: 0, height: 0 } }
            animate={ { opacity: 1, height: "auto" } }
            exit={ { opacity: 0, height: 0 } }
            transition={ { duration: 0.2 } }
            className={ "mt-4" }
        >
            <Alert variant={ "destructive" }>
                <Trash2/>
                <AlertTitle>
                    Are you sure you want to delete this item?
                </AlertTitle>
                <AlertDescription>
                    <p>This actions cannot be undone</p>
                    <div className={ "flex justify-between mt-4 w-full items-center" }>
                        <Button variant={ "destructive" } disabled={ isPending }
                                onClick={ handleDeleteItem }>
                            Delete Item
                        </Button>
                        <Button variant={ "outline" } onClick={ toggleShowConfirmDelete }>
                            Cancel
                        </Button>
                    </div>
                </AlertDescription>
            </Alert>
        </motion.div>

    )
}
