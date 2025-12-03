import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useGetShopById } from "@/api/generated/shops/shops.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import { ShipmentPieChart } from "@/components/shop/ShipmentPieChart.tsx";
import { WarehouseBarChart } from "@/components/shop/WarehouseBarChart.tsx";

type ShopDetailProps = {
    shopId: string
}

export default function ShopDetail( { shopId }: ShopDetailProps ) {
    const {
        data: shopResponse,
        isLoading,
        isError,
        error,
    } = useGetShopById( shopId, { query: { enabled: !!shopId } } );

    if ( isLoading ) {
        return <Loading title="Loading shop data"/>;
    }

    if ( isError || !shopResponse ) {
        throw error;
    }

    return <Card className="w-full">
        <CardHeader>
            <small className="text-xs uppercase leading-0">Shop</small>
            <CardTitle className="text-2xl">{ shopResponse.data.name }</CardTitle>
        </CardHeader>
        <CardContent>
            <Tabs defaultValue="piechart">
                <TabsList>
                    <TabsTrigger value="piechart">Shipment Status</TabsTrigger>
                    <TabsTrigger value="barchart">Warehouse Capacities</TabsTrigger>
                </TabsList>
                <TabsContent value="piechart">
                    <ShipmentPieChart shopId={ shopId }/>
                </TabsContent>
                <TabsContent value="barchart">
                    <WarehouseBarChart shopId={ shopId }/>
                </TabsContent>

            </Tabs>
        </CardContent>
        <CardFooter>
            <small>ID: { shopResponse.data.id }</small>
        </CardFooter>
    </Card>
}