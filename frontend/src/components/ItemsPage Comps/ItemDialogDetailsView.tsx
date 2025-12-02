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
import { useQueryClient } from "@tanstack/react-query";
import { getGetAllInventoryQueryKey } from "@/api/generated/inventory/inventory.ts";
import DeleteItemAlert from "@/components/ItemsPage Comps/DeleteItemAlert.tsx";
import EditItem from "@/components/ItemsPage Comps/EditItem.tsx";
import { AnimatePresence } from "framer-motion";


export default function ItemDialogDetailsView( { item, itemQuantity }: Readonly<{
    item: Item,
    itemQuantity?: ItemQuantityType
}> ) {

    const [ dialogOpen, setDialogOpen ] = useState( false );

    const [ showConfirmDelete, setShowConfirmDelete ] = useState( false );

    const [ showEditItem, setShowEditItem ] = useState( false );

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

    const handleOpenChange = ( open: boolean ) => {
        setShowConfirmDelete( false );
        setShowEditItem( false )
        setDialogOpen( open );
    }

    const toggleShowEditItem = () => {
        setShowEditItem( !showEditItem );
    }

    return (
        <Dialog open={ dialogOpen } onOpenChange={ handleOpenChange }>
            <DialogTrigger asChild>
                <Button variant={ "link" }>
                    Details
                </Button>
            </DialogTrigger>
            <DialogContent className={ "max-h-[90vh] overflow-y-auto" }>
                <DialogHeader>
                    <DialogTitle className={ "text-lg text-accent-foreground font-medium" }>
                        { item.name }
                    </DialogTitle>
                    <DialogDescription className={ "text-muted-foreground" }>
                        SKU: { item.sku || "-- no SKU --" }
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
                    <Button onClick={ toggleShowEditItem } disabled={ showConfirmDelete }>Edit Item</Button>
                    <Button disabled={ showEditItem } variant={ "destructive" } onClick={ toggleShowConfirmDelete }>
                        Delete Item
                    </Button>
                </div>
                <AnimatePresence>
                    { showConfirmDelete &&
                        <DeleteItemAlert isPending={ isPending } onDeleteItem={ handleDeleteItem }
                                         toggleShowConfirmDelete={ toggleShowConfirmDelete }/>
                    }
                    { showEditItem &&
                        <EditItem item={ item } closeEdit={ toggleShowEditItem }/>
                    }
                </AnimatePresence>
            </DialogContent>
        </Dialog>
    )
}
