import { TrendingUp } from "lucide-react"
import { Pie, PieChart } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    type ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart"
import { useGetAllShipmentsByShopId } from "@/api/generated/shipments/shipments.ts";
import Loading from "@/components/custom-ui/Loading.tsx";


type ShipmentPieChartProps = {
    shopId: string;
}

type ChartData = {
    status: string;
    amount: number;
    fill: string;
}

const chartConfig = {
    ORDERED: {
        label: "Ordered",
        color: "var(--chart-1)",
    },
    PROCESSED: {
        label: "Processed",
        color: "var(--chart-2)",
    },
    IN_DELIVERY: {
        label: "In delivery",
        color: "var(--chart-3)",
    },
    COMPLETED: {
        label: "Completed",
        color: "var(--chart-4)",
    }
} satisfies ChartConfig

export function ShipmentPieChart( { shopId }: ShipmentPieChartProps ) {
    const { data: shipmentResponse, isError, error, isLoading } = useGetAllShipmentsByShopId( shopId );

    if ( isLoading ) {
        return <Loading classNames="flex flex-col items-center justify-center" title="Loading shipment data"/>
    }

    if ( isError || !shipmentResponse ) {
        throw error;
    }

    const shipments = shipmentResponse.data;

    const groupedShipments = Object.groupBy( shipments, ( shipment ) => shipment.status );
    const chartData: ChartData[] = Object.entries( groupedShipments ).map( ( [ key, value ] ) => ( {
        status: key,
        amount: value?.length,
        fill: chartConfig[ key as keyof typeof chartConfig ].color
    } ) )

    return (
        <Card className="flex flex-col">
            <CardHeader className="items-center pb-0">
                <CardTitle>Shipment Pie Chart</CardTitle>
                <CardDescription>Amount of shipments by status</CardDescription>
            </CardHeader>
            <CardContent className="flex-1 pb-0">
                <ChartContainer
                    config={ chartConfig }
                    className="[&_.recharts-pie-label-text]:fill-foreground mx-auto aspect-square max-h-[400px] pb-0"
                >
                    <PieChart>
                        <ChartTooltip content={ <ChartTooltipContent/> }/>
                        <Pie data={ chartData } dataKey="amount" label nameKey="status"/>
                    </PieChart>
                </ChartContainer>
            </CardContent>
            <CardFooter className="flex-col gap-2 text-sm">
                <div className="flex items-center gap-2 leading-none font-medium">
                    { shipments.length } shipments this year and growing <TrendingUp className="h-4 w-4"/>
                </div>
                <div className="text-muted-foreground leading-none">
                    Showing shipments by status
                </div>
            </CardFooter>
        </Card>
    )
}
