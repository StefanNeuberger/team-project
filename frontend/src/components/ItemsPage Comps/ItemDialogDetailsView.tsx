import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Button } from "@/components/ui/button.tsx";
import type { ItemQuantityType } from "@/pages/ItemsPage.tsx";
import { DialogDescription } from "@radix-ui/react-dialog";
import { NavLink, useParams } from "react-router-dom";
import { Separator } from "@/components/ui/separator.tsx";
import { getGetAllItemsQueryKey, useDeleteItemById } from "@/api/generated/items/items.ts";
import { toast } from "sonner";
import { useState } from "react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert.tsx";
import { Trash2 } from "lucide-react"
import { motion } from "framer-motion";
import { useQueryClient } from "@tanstack/react-query";
import { getGetAllInventoryQueryKey } from "@/api/generated/inventory/inventory.ts";


export default function ItemDialogDetailsView( { item, itemQuantity }: Readonly<{
    item: Item,
    itemQuantity?: ItemQuantityType
}> ) {

    const [ dialogOpen, setDialogOpen ] = useState( false );

    const [ showConfirmDelete, setShowConfirmDelete ] = useState( false );

    const { shopId } = useParams();

    const { mutate: deleteItem, isPending } = useDeleteItemById();

    const queryClient = useQueryClient();

    const formatDate = ( isoDate: string ) => {
        return new Intl.DateTimeFormat( 'de-DE', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        } ).format( new Date( isoDate ) );
    };

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

                    setDialogOpen( false )
                },
                onError: ( error ) => {
                    toast.error( error.response?.data.message || error.message || "Failed to delete item." );
                    console.error( "Failed to delete item:", error );
                }
            }
        );
    }

    const toggleShowConfirmDelete = () => {
        setShowConfirmDelete( !showConfirmDelete );
    }

    const itemArray = Object.entries( itemQuantity || {} );

    return (
        <Dialog open={ dialogOpen } onOpenChange={ setDialogOpen }>
            <DialogTrigger asChild>
                <Button variant={ "link" }>
                    Details
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle className={ "text-lg text-accent-foreground font-medium" }>
                        { item.name }
                    </DialogTitle>
                    <DialogDescription className={ "text-muted-foreground" }>
                        Detail view of Item
                    </DialogDescription>
                </DialogHeader>
                <p className={ "text-muted-foreground text-center" }>-- no image available --</p>
                <Separator/>
                <div className={ "grid grid-cols-2 gap-2" }>
                    <p className={ "text-muted-foreground" }>Total #:</p>
                    <p>{ itemQuantity?.totalQuantity }</p>
                    <p className={ "text-muted-foreground" }>Created:</p>
                    <p>{ formatDate( item.createdDate ) }</p>
                    { item.lastModifiedDate && item.lastModifiedDate !== item.createdDate &&
                        <>
                            <p className={ "text-muted-foreground" }>Modified:</p>
                            <p>{ formatDate( item.lastModifiedDate ) }</p>
                        </>
                    }

                </div>
                <Separator/>
                <div className={ "grid grid-cols-2 gap-4" }>
                    {
                        itemArray.map( ( [ warehouseName, quantity ] ) => {

                            if ( warehouseName === "totalQuantity" ) {
                                return null;
                            }
                            return (
                                <div key={ warehouseName }
                                     className={ "flex flex-col text-center gap-2 justify-center items-center" }>
                                    <p key={ warehouseName } className={ "text-center" }>
                                    </p>
                                    <span
                                        className={ "text-muted-foreground" }>{ warehouseName }: </span> { quantity }
                                </div>
                            );
                        } )
                    }
                    { itemArray.length === 1 &&
                        <p className={ "text-center col-span-full text-muted-foreground my-4" }>--in no warehouses
                            available--</p>
                    }
                </div>
                <Separator className={ "my-4" }/>
                <div className={ "flex justify-between items-center" }>
                    <Button asChild>
                        <NavLink to={ `/shop/${ shopId }/warehouses` }>See Warehouses</NavLink>
                    </Button>
                    <Button variant={ "destructive" } onClick={ toggleShowConfirmDelete }>
                        Delete Item
                    </Button>
                </div>
                { showConfirmDelete &&
                    <motion.div
                        initial={ { opacity: 0, scale: 0.9 } }
                        animate={ { opacity: 1, scale: 1 } }
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
                }

            </DialogContent>
        </Dialog>
    )
}
