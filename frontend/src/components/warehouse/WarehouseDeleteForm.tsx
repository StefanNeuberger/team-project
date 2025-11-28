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
import { Button } from "@/components/ui/button.tsx";
import {
    getGetAllWarehousesQueryKey,
    getGetWarehouseQueryKey, useDeleteWarehouse,
} from "@/api/generated/warehouses/warehouses.ts";
import { Alert, AlertTitle, AlertDescription } from "@/components/ui/alert";
import { useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { FieldGroup } from "@/components/ui/field.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { useNavigate, useParams } from "react-router-dom";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";

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
                    if ( error?.response?.status === 409 ) {
                        console.error( "Failed to delete warehouse:", error );
                        form.setError( "name", {
                            type: "manual",
                            message: error.response.data.message,
                        } );
                    } else {
                        // For other errors, show generic error
                        console.error( "Failed to delete warehouse:", error );
                    }
                }
            }
        )
    }

    const handleCancel = () => navigate( -1 );


    return <Card className="w-full">
        <CardHeader>
            <CardTitle>Delete Warehouse</CardTitle>
            <CardDescription>Confirm the dialog below to delete this warehouse.</CardDescription>
        </CardHeader>
        <CardContent>
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleEditWarehouse ) } className="space-y-8">
                    <FieldGroup>
                        <div className="text-xl text-center">
                            Are you sure that you want to delete
                            warehouse <strong>{ warehouse.name }</strong> ?
                        </div>
                    </FieldGroup>
                    { deleteWarehouse.isError && !form.formState.errors.name && (
                        <FieldGroup>
                            <Alert variant="destructive">
                                <AlertCircleIcon/>
                                <AlertTitle>Oops! We encountered an error.</AlertTitle>
                                <AlertDescription>
                                    Failed to update warehouse. Please try again.
                                </AlertDescription>
                            </Alert>
                        </FieldGroup>
                    ) }
                    { deleteWarehouse.isSuccess && (
                        <FieldGroup>
                            <Alert variant="default" className="**:text-green-500 border-green-500">
                                <CheckCircle2Icon/>
                                <AlertTitle>Success! Your changes have been saved</AlertTitle>
                            </Alert>
                        </FieldGroup>
                    ) }
                    <FieldGroup className="flex flex-row items-center justify-center p-0!">
                        <Button type="submit" variant="destructive" className="basis-1/2"
                                disabled={ deleteWarehouse.isPending || deleteWarehouse.isSuccess }>
                            { deleteWarehouse.isPending ?
                                <>
                                    { "Deleting" }
                                    <Spinner/>
                                </> : "Delete" }
                        </Button>
                        <Button type="button" variant="default" onClick={ handleCancel }
                                disabled={ deleteWarehouse.isPending || deleteWarehouse.isSuccess }
                                className="basis-1/2">Cancel</Button>
                    </FieldGroup>
                </form>
            </Form>
        </CardContent>
    </Card>
}