import { Link, useParams } from "react-router-dom";
import WarehouseTable from "@/components/warehouse/WarehouseTable.tsx";
import { useGetAllWarehouses } from "@/api/generated/warehouses/warehouses.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import WarehouseMap from "@/components/warehouse/WarehouseMap.tsx";
import { Button } from "@/components/ui/button.tsx";

export default function WarehousesPage() {
    const { shopId } = useParams();
    const { data: warehouseData, isError, isLoading, error } = useGetAllWarehouses();

    if ( isLoading ) {
        return <Loading classNames="flex flex-col justify-center items-center w-full"/>;
    }

    if ( isError || !warehouseData ) {
        throw error;
    }

    return <div className="p-8 flex flex-col justify-start items-center gap-10">
        <WarehouseMap shopId={ shopId! } warehouses={ warehouseData.data }/>
        <Link to={ `/shop/${ shopId }/warehouses/create` } className="self-end">
            <Button type="button" variant="default">Create new warehouse</Button>
        </Link>
        <WarehouseTable shopId={ shopId! } data={ warehouseData.data }/>
    </div>
}