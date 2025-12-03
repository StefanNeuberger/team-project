"use client"

import { Bar, BarChart, CartesianGrid, LabelList, XAxis, YAxis } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    type ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart"
import { useGetAllWarehouses } from "@/api/generated/warehouses/warehouses.ts";
import Loading from "@/components/custom-ui/Loading.tsx";

export const description = "A bar chart"

const chartConfig = {
    capacity: {
        label: "Capacity",
    },
} satisfies ChartConfig

type WarehouseBarChartProps = {
    shopId: string;
}

export function WarehouseBarChart( { shopId }: WarehouseBarChartProps ) {
    const { data: warehouseResponse, isError, error, isLoading } = useGetAllWarehouses();

    if ( isLoading ) {
        return <Loading classNames="flex flex-col items-center justify-center" title="Loading warehouse data"/>
    }

    if ( isError || !warehouseResponse ) {
        throw error;
    }

    const warehouses = warehouseResponse.data.filter( warehouse => warehouse.shop.id === shopId );

    const chartData = warehouses.map( ( warehouse, index ) => {
        const color = `var(--chart-${ ( ( index - 1 ) % 6 ) + 1 })`;
        return { name: warehouse.name, capacity: warehouse.maxCapacity, fill: color };
    } )

    return (
        <Card>
            <CardHeader>
                <CardTitle>Warehouse Bar Chart</CardTitle>
                <CardDescription>Maximum capacity by warehouse</CardDescription>
            </CardHeader>
            <CardContent>
                <ChartContainer config={ chartConfig } className="max-h-[400px] mx-auto w-full">
                    <BarChart accessibilityLayer data={ chartData } layout={ "vertical" }
                              margin={ { right: 50, left: 50, top: 0, bottom: 0 } }>
                        <CartesianGrid horizontal={ false }/>
                        <YAxis
                            dataKey="name"
                            type="category"
                            tickLine={ false }
                            tickMargin={ 30 }
                            axisLine={ false }
                        />
                        <XAxis dataKey="capacity" type="number" hide/>
                        <ChartTooltip
                            cursor={ false }
                            content={ <ChartTooltipContent indicator="line"/> }
                        />
                        <Bar dataKey="capacity" fill="var(--chart-1)" radius={ 4 } layout="vertical">
                            <LabelList
                                dataKey="capacity"
                                position="right"
                                offset={ 8 }
                                className="fill-foreground"
                                fontSize={ 12 }
                            />
                        </Bar>
                    </BarChart>
                </ChartContainer>
            </CardContent>
        </Card>
    )
}
