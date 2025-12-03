import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { useGetAllShipmentLineItemsByShipmentId } from "@/api/generated/shipment-line-items/shipment-line-items.ts";
import type { ShipmentResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Separator } from "@/components/ui/separator.tsx";


type ShipmentDetailsViewProps = {
    shipment: ShipmentResponseDTO;
}

export default function ShipmentDetails( { shipment }: Readonly<ShipmentDetailsViewProps> ) {

    const {
        data: shipmentLineItems
    } = useGetAllShipmentLineItemsByShipmentId( shipment.id || "" );

    console.log( shipmentLineItems );

    return (
        <Dialog>
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
                        <div className={ "text-sm text-muted-foreground" }>
                            Expected Quantity: { lineItem.expectedQuantity }
                        </div>
                        <div className={ "text-sm text-muted-foreground" }>
                            Received Quantity: { lineItem.receivedQuantity }
                        </div>
                    </div>
                ) }

            </DialogContent>

        </Dialog>
    )
}
