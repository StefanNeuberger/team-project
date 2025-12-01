import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import {
    Form,
} from "@/components/ui/form";
import {
    getGetAllWarehousesQueryKey,
    usePostWarehouse
} from "@/api/generated/warehouses/warehouses.ts";
import { useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import WarehouseForm from "@/components/warehouse/WarehouseForm.tsx";
import { type WarehouseFormData, warehouseFormSchema } from "@/types/warehouse.ts";

type WarehouseCreateFormProps = {
    shopId: string;
}

export default function WarehouseCreateForm( { shopId }: WarehouseCreateFormProps ) {
    const queryClient = useQueryClient();
    const form = useForm<WarehouseFormData>( {
        resolver: zodResolver( warehouseFormSchema ),
        defaultValues: {
            name: "",
            maxCapacity: 1,
            street: "",
            number: "",
            city: "",
            postalCode: "",
            state: "",
            country: "",
            lat: 0.0,
            lng: 0.0
        }
    } );


    const postWarehouse = usePostWarehouse();


    const handleEditWarehouse = ( data: WarehouseFormData ) => {
        postWarehouse.mutate(
            {
                data: {
                    name: data.name,
                    shopId: shopId,
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
                onSuccess: () => {
                    queryClient.invalidateQueries( {
                        queryKey: getGetAllWarehousesQueryKey()
                    } )
                },
                onError: ( error ) => {
                    if ( error?.response?.status === 409 && error?.response?.data && typeof error.response.data === "object" && "message" in error.response.data ) {
                        form.setError( "name", {
                            type: "manual",
                            message: error.response.data.message as string
                        } );
                    }
                }
            }
        )
    }

    return <Card className="w-full">
        <CardHeader>
            <CardTitle>Create Warehouse</CardTitle>
            <CardDescription>Fill out the fields and click save to create a new warehouse.</CardDescription>
        </CardHeader>
        <CardContent>
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleEditWarehouse ) } className="space-y-8">
                    <WarehouseForm<typeof form>
                        form={ form }
                        mode={ "CREATE" }
                        isError={ postWarehouse.isError }
                        isSuccess={ postWarehouse.isSuccess }
                        isPending={ postWarehouse.isPending }/>

                </form>
            </Form>
        </CardContent>
    </Card>
}