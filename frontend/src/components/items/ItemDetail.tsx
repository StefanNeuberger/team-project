import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import { useGetInventoryByItemId } from "@/api/generated/inventory/inventory.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs.tsx";
import ItemTable from "@/components/items/ItemTable.tsx";

type ItemDetailProps = {
    shopId: string;
    item: Item;
}

export default function ItemDetail( { shopId, item }: ItemDetailProps ) {
    const { data: inventoryData, isError, isLoading } = useGetInventoryByItemId( item.id );

    return <Card className="w-full">
        <CardHeader>
            <small className="text-xs uppercase leading-0">Item</small>
            <CardTitle className="text-2xl">{ item.name }</CardTitle>
            <CardDescription>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>Name</span>
                        <p>{ item.name }</p>
                    </div>
                    <div>
                        <span>SKU</span>
                        <p>{ item.sku }</p>
                    </div>
                </div>
            </CardDescription>
        </CardHeader>
        <CardContent>
            <Tabs defaultValue="related">
                <TabsList>
                    <TabsTrigger value="related">Inventories</TabsTrigger>
                </TabsList>
                <TabsContent value="related">
                    { isLoading ?
                        <Loading classNames="flex flex-col items-center justify-center"
                                 title="Related data is loading"/> : null }
                    { isError && !inventoryData ? <p>Could not load related data</p> : null }
                    { !isError && inventoryData ? <ItemTable shopId={ shopId }
                                                             data={ inventoryData.data }/>
                        : null }
                </TabsContent>
            </Tabs>

        </CardContent>
        <CardFooter>
            <small>ID: { item.id }</small>
        </CardFooter>
    </Card>
}