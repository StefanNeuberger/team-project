import { useParams } from "react-router-dom";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs.tsx";
import { useGetAllInventory } from "@/api/generated/inventory/inventory.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import InventoryTable from "@/components/inventory/InventoryTable.tsx";

export default function InventoryPage() {
    const { shopId } = useParams();
    const { data: inventoryData, isError, isLoading, error } = useGetAllInventory();

    if ( isLoading ) {
        return <Loading title="Inventory is loading"/>;
    }

    if ( isError || !inventoryData ) {
        throw error;
    }

    return <div className="p-8 w-full">
        <Tabs defaultValue="table">
            <div className="flex flex-row justify-between items-center">
                <TabsList className="h-9">
                    <TabsTrigger className="min-w-20" value="table">Table</TabsTrigger>
                </TabsList>
            </div>
            <TabsContent value="table">
                <InventoryTable shopId={ shopId! } data={ inventoryData.data } showWarehouse/>
            </TabsContent>
        </Tabs>
    </div>
}