import { useParams } from "react-router-dom";
import { useGetWarehouse } from "@/api/generated/warehouses/warehouses.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import WarehouseDeleteForm from "@/components/warehouse/WarehouseDeleteForm.tsx";

export default function WarehouseDeletePage() {
    const { id } = useParams();
    const { data: warehouseData, isError, error, isLoading } = useGetWarehouse( id! );

    if ( isLoading ) {
        return <Loading classNames="flex flex-col justify-center items-center w-full"/>;
    }

    if ( isError || !warehouseData ) {
        throw error;
    }

    return <div className="p-8 flex flex-col justify-start items-center">
        <WarehouseDeleteForm warehouse={ warehouseData.data }/>
    </div>
}