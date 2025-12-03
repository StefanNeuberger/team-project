import type { ShipmentResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableFooter,
    TableHead,
    TableHeader,
    TableRow
} from "@/components/ui/table.tsx";
import { motion } from "framer-motion";
import ShipmentDetails from "@/components/shipments/ShipmentDetails.tsx";
import LineItemCreateDialog from "@/components/shipments/LineItemCreateDialog.tsx";
import ShipmentUpdateOrderForm from "@/components/shipments/ShipmentUpdateOrderForm.tsx";

type ShipmentsTableProps = {
    shipments: ShipmentResponseDTO[] | undefined;
}

export default function ShipmentsTable( { shipments }: Readonly<ShipmentsTableProps> ) {

    return (
        <>
            { shipments?.length == 0 ?
                <div className={ "flex-1" }>
                    <p>-- No Shipments yet -- </p>
                </div>
                :
                <div className={ "m-auto px-4" }>
                    <small>Total Shipments: { shipments?.length ?? "N/A" }</small>
                    <Table className={ "my-4" }>
                        { ( !shipments || shipments?.length > 0 ) && <TableCaption>All Shipments</TableCaption> }
                        <TableHeader>
                            <TableRow className={ "border-muted-foreground" }>
                                <TableHead className={ "w-[100px]" }>Nr.</TableHead>
                                <TableHead className={ "w-screen max-w-[600px]" }>ID</TableHead>
                                <TableHead className={ "w-[100px]" }>Destination</TableHead>
                                <TableHead className={ "w-[100px]" }>Expected</TableHead>
                                <TableHead className={ "w-[100px]" }>Status</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            { shipments?.map( ( shipment: ShipmentResponseDTO, index ) => (
                                <motion.tr
                                    className={ "border-b-[0.5px] border-muted-foreground hover:bg-muted" }
                                    key={ shipment.id }
                                    initial={ { opacity: 0, x: -20 } }
                                    animate={ { opacity: 1, x: 0 } }
                                >
                                    <TableCell
                                        className={ "font-medium py-3 text-muted-foreground" }>
                                        { index + 1 }
                                    </TableCell>
                                    <TableCell
                                        className={ " flex justify-between items-center " }>
                                        { shipment.id }
                                        <div>
                                            <LineItemCreateDialog shipmentId={ shipment.id || "" }
                                                                  status={ shipment.status }/>
                                            <ShipmentDetails shipment={ shipment }/>
                                        </div>
                                    </TableCell>
                                    <TableCell
                                        className={ "font-medium text-muted-foreground" }>
                                        { shipment.warehouse.name }
                                    </TableCell>
                                    <TableCell
                                        className={ "font-medium text-muted-foreground" }>
                                        { shipment.expectedArrivalDate }
                                    </TableCell>
                                    <TableCell
                                        className={ "font-medium text-muted-foreground" }>
                                        <ShipmentUpdateOrderForm status={ shipment.status }
                                                                 shipmentId={ shipment.id || "" }
                                        />
                                    </TableCell>
                                </motion.tr>
                            ) ) }
                        </TableBody>
                        <TableFooter className={ "border-0" }>
                            <TableRow></TableRow>
                        </TableFooter>
                    </Table>
                </div>
            }
        </>
    )
}
