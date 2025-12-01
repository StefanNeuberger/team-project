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
    getGetWarehouseQueryKey, useDeleteWarehouse,
} from "@/api/generated/warehouses/warehouses.ts";
import { useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { FieldGroup } from "@/components/ui/field.tsx";
import { useNavigate, useParams } from "react-router-dom";
import WarehouseForm from "@/components/warehouse/WarehouseForm.tsx";

type WarehouseEditFormProps = {
    warehouse: WarehouseResponseDTO;
}

export default function WarehouseDeleteForm( { warehouse }: WarehouseEditFormProps ) {
    const { shopId } = useParams();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const form = useForm();

    const deleteWarehouse = useDeleteWarehouse();

    const handleEditWarehouse = () => {
        deleteWarehouse.mutate(
            {
                id: warehouse.id
            },
            {
                onSuccess: () => {
                    queryClient.invalidateQueries( {
                        queryKey: getGetAllWarehousesQueryKey()
                    } )
                    queryClient.invalidateQueries( {
                        queryKey: getGetWarehouseQueryKey( warehouse.id ),
                    } )
                    navigate( `/shop/${ shopId }/warehouses` );
                },
                onError: ( error ) => {
                    console.error( "Failed to delete warehouse:", error );
                }
            }
        )
    }


    return <Card className="w-full">
        <CardHeader>
            <CardTitle>Delete Warehouse</CardTitle>
            <CardDescription>Confirm the dialog below to delete this warehouse.</CardDescription>
        </CardHeader>
        <CardContent>
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleEditWarehouse ) } className="space-y-8">
                    <WarehouseForm<typeof form> form={ form } isError={ deleteWarehouse.isError }
                                                isSuccess={ deleteWarehouse.isSuccess }
                                                isPending={ deleteWarehouse.isPending } mode={ "DELETE" }>
                        <FieldGroup key="children">
                            <div className="text-xl text-center">
                                Are you sure that you want to delete
                                warehouse <strong>{ warehouse.name }</strong> ?
                            </div>
                        </FieldGroup>
                    </WarehouseForm>
                </form>
            </Form>
        </CardContent>
    </Card>
}