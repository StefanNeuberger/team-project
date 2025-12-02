import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Button } from "@/components/ui/button.tsx";
import type { ItemQuantityType } from "@/pages/ItemsPage.tsx";
import { DialogDescription } from "@radix-ui/react-dialog";
import { NavLink, useParams } from "react-router-dom";
import { Separator } from "@/components/ui/separator.tsx";
import { useState } from "react";
import DeleteItemAlert from "@/components/ItemsPage Comps/DeleteItemAlert.tsx";
import EditItem from "@/components/ItemsPage Comps/EditItem.tsx";
import { AnimatePresence } from "framer-motion";


export default function ItemDialogDetailsView( { item, itemQuantity }: Readonly<{
    item: Item,
    itemQuantity?: ItemQuantityType
}> ) {

    const [ dialogOpen, setDialogOpen ] = useState( false );

    const [ showDeleteItem, setShowDeleteItem ] = useState( false );

    const [ showEditItem, setShowEditItem ] = useState( false );

    const { shopId } = useParams();

    const formatDate = ( isoDate: string ) => {
        return new Intl.DateTimeFormat( 'de-DE', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        } ).format( new Date( isoDate ) );
    };

    const toggleShowConfirmDelete = () => {
        setShowDeleteItem( !showDeleteItem );
    }


    const handleCloseDialog = () => {
        setDialogOpen( false );
    }

    const handleOpenChange = ( open: boolean ) => {
        setShowDeleteItem( false );
        setShowEditItem( false )
        setDialogOpen( open );
    }

    const toggleShowEditItem = () => {
        setShowEditItem( !showEditItem );
    }

    const itemArray = Object.entries( itemQuantity || {} );

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
                    <Button onClick={ toggleShowEditItem } disabled={ showDeleteItem }>Edit Item</Button>
                    <Button disabled={ showEditItem } variant={ "destructive" } onClick={ toggleShowConfirmDelete }>
                        Delete Item
                    </Button>
                </div>
                <AnimatePresence>
                    { showDeleteItem &&
                        <DeleteItemAlert item={ item }
                                         closeDialog={ handleCloseDialog }
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
