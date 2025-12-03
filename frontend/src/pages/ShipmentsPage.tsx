import { Navigate, useParams } from "react-router-dom";
import { useGetAllShipmentsByShopId } from "@/api/generated/shipments/shipments.ts";
import ShipmentCreateDialog from "@/components/shipments/ShipmentCreateDialog.tsx";
import ShipmentsTable from "@/components/shipments/ShipmentsTable.tsx";

export default function ShipmentsPage() {
    const { shopId } = useParams();

    const { data: shopShipments } = useGetAllShipmentsByShopId( shopId || "" );

    if ( !shopId ) {
        return <Navigate to={ "/" }/>
    }

    return (
        <div className={ "flex justify-center items-center mt-8 flex-col gap-8" }>
            <h1 className={ "text-2xl text-center text-accent-foreground text-bold" }>Shipments</h1>
            <div className={ "w-full flex px-4" }>
                <ShipmentCreateDialog/>
            </div>
            <ShipmentsTable shipments={ shopShipments?.data }/>
        </div>
    )
}