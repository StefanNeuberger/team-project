import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import {
    Card,
    CardAction,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import WarehouseMap from "@/components/warehouse/WarehouseMap.tsx";
import { useGetAll } from "@/api/generated/inventory/inventory.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import InventoryTable from "@/components/inventory/InventoryTable.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Link } from "react-router-dom";

type WarehouseDetailProps = {
    shopId: string;
    warehouse: WarehouseResponseDTO;
}

export default function WarehouseDetail( { shopId, warehouse }: WarehouseDetailProps ) {
    const { data: inventoryData, isError, isLoading } = useGetAll();

    return <Card className="w-full">
        <CardHeader>
            <CardTitle className="text-2xl">{ warehouse.name }</CardTitle>
            <CardDescription>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>Shop</span>
                        <p>{ warehouse.shop.name }</p>
                    </div>
                    <div>
                        <span>Address</span>
                        <p>{ Object.values( warehouse.address || {} ).join( ", " ) }</p>
                    </div>
                    <div>
                        <span>Max Capacity</span>
                        <p>{ Intl.NumberFormat( "de", {} ).format( warehouse.maxCapacity ) + " Units" }</p>
                    </div>
                </div>
            </CardDescription>
            <CardAction className="space-x-2">
                <Link to={ `/shop/${ shopId }/warehouses/${ warehouse.id }/edit` }>
                    <Button variant="default">Edit</Button>
                </Link>
                <Link to={ `/shop/${ shopId }/warehouses/${ warehouse.id }/delete` }>
                    <Button variant="destructive">Delete</Button>
                </Link>
            </CardAction>
        </CardHeader>
        <CardContent>
            <Tabs defaultValue="map">
                <TabsList>
                    <TabsTrigger value="map">Location</TabsTrigger>
                    <TabsTrigger value="related">Inventories</TabsTrigger>
                </TabsList>
                <TabsContent value="map">
                    <WarehouseMap shopId={ shopId } warehouses={ [ warehouse ] } centerOnFirst/>
                </TabsContent>
                <TabsContent value="related">
                    { isLoading || isError || !inventoryData ?
                        <Loading classNames="flex flex-col items-center justify-center" title="Loading inventory"/> :
                        <InventoryTable shopId={ shopId }
                                        data={ inventoryData.data.filter( data => data.warehouse.id === warehouse.id ) }/> }
                </TabsContent>
            </Tabs>
        </CardContent>
        <CardFooter>
            <small>ID: { warehouse.id }</small>
        </CardFooter>
    </Card>
}