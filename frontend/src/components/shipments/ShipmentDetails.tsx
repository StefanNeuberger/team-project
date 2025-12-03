import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import {
    getGetAllShipmentLineItemsByShipmentIdQueryKey,
    useDeleteShipmentLineItem,
    useGetAllShipmentLineItemsByShipmentId
} from "@/api/generated/shipment-line-items/shipment-line-items.ts";
import type { ShipmentResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Separator } from "@/components/ui/separator.tsx";
import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import LineItemUpdateForm from "@/components/shipments/LineItemUpdateForm.tsx";


type ShipmentDetailsViewProps = {
    shipment: ShipmentResponseDTO;
}

export default function ShipmentDetails( { shipment }: Readonly<ShipmentDetailsViewProps> ) {


    const [ editingItemIds, setEditingItemIds ] = useState<Set<string>>( new Set() );

    const queryClient = useQueryClient();
    const deleteLineItem = useDeleteShipmentLineItem();

    const {
        data: shipmentLineItems
    } = useGetAllShipmentLineItemsByShipmentId( shipment.id || "" );

    const toggleEditMode = ( itemId: string ) => {
        setEditingItemIds( prev => {
            const newSet = new Set( prev );
            if ( newSet.has( itemId ) ) {
                newSet.delete( itemId );
            } else {
                newSet.add( itemId );
            }
            return newSet;
        } );
    };

    const isEditing = ( itemId: string ) => editingItemIds.has( itemId );

    const handleDeleteLineItem = ( lineItemId: string ) => {
        deleteLineItem.mutate( { id: lineItemId }, {
            onSuccess: () => {
                toast.success( "Line item deleted successfully" );
                console.log( "Deleted line item" );
                queryClient.removeQueries( {
                    queryKey: getGetAllShipmentLineItemsByShipmentIdQueryKey( shipment.id || "" )
                } );
            },
            onError: ( error ) => {
                console.error( "Error deleting line item", error );
                toast.error( error.response?.data.message || error.message || "Failed to delete item." );
            }
        } );
    };

    const handleOpenChange = () => {
        setEditingItemIds( new Set() );
    }

    return (
        <Dialog onOpenChange={ handleOpenChange }>
            <DialogTrigger asChild>
                <Button variant={ "link" }>
                    Details
                </Button>
            </DialogTrigger>
            <DialogContent className={ "max-h-[90vh] overflow-y-auto" }>
                <DialogHeader>
                    <DialogTitle>
                        Shipment Details
                    </DialogTitle>
                    <DialogDescription>
                        Here are the details of the shipment.
                    </DialogDescription>
                </DialogHeader>
                <div className={ "grid grid-cols-2 gap-2" }>
                    <p className={ "text-muted-foreground" }>Expected:</p>
                    <p>{ shipment.expectedArrivalDate }</p>
                    <p className={ "text-muted-foreground" }>Status:</p>
                    <p>{ shipment.status }</p>
                    <p className={ "text-muted-foreground" }>Warehouse:</p>
                    <p>{ shipment.warehouse.name }</p>
                </div>
                <Separator orientation={ "horizontal" }/>
                <h2>OrderedItems</h2>
                { !shipmentLineItems?.data?.length && (
                    <div className={ "p-4 text-sm text-muted-foreground" }>
                        No items in this shipment.
                    </div>
                ) }
                { shipmentLineItems?.data?.map( ( lineItem ) =>
                    <div key={ lineItem.id } className={ "p-4 border-b" }>
                        <div className={ "font-medium" }>{ lineItem?.item?.name }</div>

                        { isEditing( lineItem.id || "" ) ?
                            <div className={ "space-y-4 my-2" }>
                                <LineItemUpdateForm lineItem={ lineItem }
                                                    shipmentId={ shipment.id || "" }
                                                    closeEditMode={ () => toggleEditMode( lineItem.id || "" ) }/>
                                <Separator/>
                            </div>
                            :
                            <>
                                <div className={ "text-sm text-muted-foreground" }>
                                    Expected Quantity: { lineItem.expectedQuantity }
                                </div>
                                <div className={ "text-sm text-muted-foreground" }>
                                    Received Quantity: { lineItem.receivedQuantity }
                                </div>
                            </>
                        }

                        { shipment.status === "COMPLETED" ? null :
                            <div className={ "flex items-center justify-between mt-4" }>
                                <Button variant={ "outline" }
                                        onClick={ () => toggleEditMode( lineItem.id || "" ) }
                                        size={ "sm" }>Edit</Button>
                                <Button variant={ "destructive" }
                                        size={ "sm" }
                                        onClick={
                                            () => handleDeleteLineItem( lineItem.id || "" ) }>
                                    Delete
                                </Button>
                            </div>
                        }
                    </div>
                ) }

            </DialogContent>

        </Dialog>
    )
}
