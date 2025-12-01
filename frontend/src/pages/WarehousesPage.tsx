import { Link, useParams } from "react-router-dom";
import WarehouseTable from "@/components/warehouse/WarehouseTable.tsx";
import { useGetAllWarehouses } from "@/api/generated/warehouses/warehouses.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import WarehouseMap from "@/components/warehouse/WarehouseMap.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs.tsx";

export default function WarehousesPage() {
    const { shopId } = useParams();
    const { data: warehouseData, isError, isLoading, error } = useGetAllWarehouses();

    if ( isLoading ) {
        return <Loading classNames="flex flex-col justify-center items-center w-full"/>;
    }

    if ( isError || !warehouseData ) {
        throw error;
    }

    return <div className="p-8 w-full">
        <Tabs defaultValue="map">
            <div className="flex flex-row justify-between items-center">
                <TabsList className="h-9">
                    <TabsTrigger className="min-w-20" value="map">Map</TabsTrigger>
                    <TabsTrigger className="min-w-20" value="table">Table</TabsTrigger>
                </TabsList>
                <Link to={ `/shop/${ shopId }/warehouses/create` } className="self-end">
                    <Button type="button" variant="default">New</Button>
                </Link>
            </div>
            <TabsContent value="map">
                <WarehouseMap shopId={ shopId! } warehouses={ warehouseData.data }/>
            </TabsContent>
            <TabsContent value="table">
                <WarehouseTable shopId={ shopId! } data={ warehouseData.data }/>
            </TabsContent>
        </Tabs>
    </div>
}