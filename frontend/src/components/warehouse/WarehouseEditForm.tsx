import {
    Card, CardAction,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import {
    Form,
} from "@/components/ui/form";
import { Button } from "@/components/ui/button.tsx";
import {
    getGetAllWarehousesQueryKey,
    getGetWarehouseQueryKey,
    usePatchWarehouse
} from "@/api/generated/warehouses/warehouses.ts";
import { useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Link, useParams } from "react-router-dom";
import WarehouseForm from "@/components/warehouse/WarehouseForm.tsx";
import { type WarehouseFormData, warehouseFormSchema } from "@/types/warehouse.ts";


type WarehouseEditFormProps = {
    warehouse: WarehouseResponseDTO;
}

export default function WarehouseEditForm( { warehouse }: WarehouseEditFormProps ) {
    const { shopId } = useParams();
    const queryClient = useQueryClient();
    const form = useForm<WarehouseFormData>( {
        resolver: zodResolver( warehouseFormSchema ),
        defaultValues: {
            name: warehouse.name,
            maxCapacity: warehouse.maxCapacity,
            street: warehouse.address?.street ?? "",
            number: warehouse.address?.number ?? "",
            city: warehouse.address?.city ?? "",
            postalCode: warehouse.address?.postalCode ?? "",
            state: warehouse.address?.state ?? "",
            country: warehouse.address?.country ?? "",
            lat: warehouse.geoLocation?.[ 0 ] ?? 0.0,
            lng: warehouse.geoLocation?.[ 1 ] ?? 0.0
        }
    } );


    const patchWarehouse = usePatchWarehouse();


    const handleEditWarehouse = ( data: WarehouseFormData ) => {
        patchWarehouse.mutate(
            {
                id: warehouse.id,
                data: {
                    name: data.name,
                    maxCapacity: data.maxCapacity,
                    street: data.street,
                    number: data.number,
                    city: data.city,
                    postalCode: data.postalCode,
                    state: data.state,
                    country: data.country,
                    lat: data.lat,
                    lng: data.lng,
                }
            },
            {
                onSuccess: ( data ) => {
                    console.log( data );
                    queryClient.invalidateQueries( {
                        queryKey: getGetAllWarehousesQueryKey()
                    } )
                    queryClient.invalidateQueries( {
                        queryKey: getGetWarehouseQueryKey( data.data.id ),
                    } )
                },
                onError: ( error ) => {
                    if ( error?.response?.status === 409 ) {
                        console.error( "Failed to update warehouse:", error );
                        form.setError( "name", {
                            type: "manual",
                            message: error.response.data.message,
                        } );
                    } else {
                        // For other errors, show generic error
                        console.error( "Failed to update warehouse:", error );
                    }
                }
            }
        )
    }


    return <Card className="w-full">
        <CardHeader>
            <CardTitle>Edit Warehouse</CardTitle>
            <CardDescription>Edit the fields and click save to update this warehouse.</CardDescription>
            <CardAction className="space-x-2">

                <Link to={ `/shop/${ shopId }/warehouses/${ warehouse.id }/delete` }>
                    <Button variant="destructive">Delete</Button>
                </Link>
            </CardAction>
        </CardHeader>
        <CardContent>
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleEditWarehouse ) } className="space-y-8">
                    <WarehouseForm<typeof form>
                        form={ form }
                        isError={ patchWarehouse.isError }
                        isSuccess={ patchWarehouse.isSuccess }
                        isPending={ patchWarehouse.isPending }
                        mode={ "UPDATE" }
                    />
                </form>
            </Form>
        </CardContent>
    </Card>
}