import { useParams } from "react-router-dom";
import { useGetInventory } from "@/api/generated/inventory/inventory.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import InventoryDetail from "@/components/inventory/InventoryDetail.tsx";

export default function InventoryDetailPage() {
    const { shopId, id } = useParams();
    const { data: inventoryData, isError, error, isLoading } = useGetInventory( id! );

    if ( isLoading ) {
        return <Loading classNames="flex flex-col justify-center items-center w-full"/>;
    }

    if ( isError || !inventoryData ) {
        throw error;
    }

    return <div className="p-8 flex flex-col justify-start items-center">
        <InventoryDetail shopId={ shopId! } inventory={ inventoryData.data }/>
    </div>
}