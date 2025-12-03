import type { InventoryResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import WarehouseMap from "@/components/warehouse/WarehouseMap.tsx";
import { Link } from "react-router-dom";

type InventoryDetailProps = {
    shopId: string;
    inventory: InventoryResponseDTO;
}

export default function InventoryDetail( { shopId, inventory }: InventoryDetailProps ) {

    return <Card className="w-full">
        <CardHeader>
            <small className="text-xs uppercase leading-0">Inventory</small>
            <CardTitle className="text-2xl">{ inventory.item.name }</CardTitle>
            <CardDescription>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>Shop</span>
                        <p>
                            <Link to={ `/shop/${ inventory.warehouse.shop.id }/warehouses/` }
                                  className="hover:underline">{ inventory.warehouse.shop.name }</Link>
                        </p>
                    </div>
                    <div>
                        <span>Warehouse</span>
                        <p>
                            <Link to={ `/shop/${ shopId }/warehouses/${ inventory.warehouse.id }` }
                                  className="hover:underline">{ inventory.warehouse.name }</Link>
                        </p>
                    </div>
                    <div>
                        <span>Warehouse Address</span>
                        <p>{ Object.values( inventory.warehouse.address || {} ).join( ", " ) }</p>
                    </div>
                    <div>
                        <span>Quantity</span>
                        <p>{ Intl.NumberFormat( "de", {} ).format( inventory.quantity ) + " Units" }</p>
                    </div>
                </div>
            </CardDescription>
        </CardHeader>
        <CardContent>
            <Tabs defaultValue="map">
                <TabsList>
                    <TabsTrigger value="map">Location</TabsTrigger>
                </TabsList>
                <TabsContent value="map">
                    <WarehouseMap shopId={ shopId } warehouses={ [ inventory.warehouse ] } centerOnFirst/>
                </TabsContent>
            </Tabs>
        </CardContent>
        <CardFooter>
            <small>ID: { inventory.id }</small>
        </CardFooter>
    </Card>
}