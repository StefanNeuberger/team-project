import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ShipmentCreateDTOStatus, type ShipmentUpdateDTOStatus } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group.tsx";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { getGetAllShipmentsByShopIdQueryKey, useUpdateShipmentStatus } from "@/api/generated/shipments/shipments.ts";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { useState } from "react";

type ShipmentUpdateOrderFormProps = {
    status: ShipmentUpdateDTOStatus;
    shipmentId: string;
}

const UpdateShipmentSchema = z.object( {
    status: z.enum( [
        ShipmentCreateDTOStatus.ORDERED,
        ShipmentCreateDTOStatus.PROCESSED,
        ShipmentCreateDTOStatus.IN_DELIVERY,
        ShipmentCreateDTOStatus.COMPLETED
    ], {
        message: 'Status is required and must be a valid value'
    } )
} )

type ShipmentUpdateOrderFormData = {
    status: ShipmentUpdateDTOStatus;
}

export default function ShipmentUpdateOrderForm( { status, shipmentId }: Readonly<ShipmentUpdateOrderFormProps> ) {


    const [ dialogOpen, setDialogOpen ] = useState( false );

    const { shopId } = useParams();

    const updateShipmentStatus = useUpdateShipmentStatus();
    const queryClient = useQueryClient();

    const form = useForm<ShipmentUpdateOrderFormData>( {
        resolver: zodResolver( UpdateShipmentSchema ),
        defaultValues: {}
    } );

    const orderStatuses = [
        { label: "Ordered", value: "ORDERED" },
        { label: "Processed", value: "PROCESSED" },
        { label: "In Delivery", value: "IN_DELIVERY" },
        { label: "Completed", value: "COMPLETED" }
    ] as { label: string; value: ShipmentCreateDTOStatus }[];

    const handleUpdateStatus = ( data: ShipmentUpdateOrderFormData ) => {
        updateShipmentStatus.mutate(
            {
                id: shipmentId,
                data: {
                    status: data.status
                }
            },
            {
                onSuccess: () => {
                    console.log( "Shipment status updated successfully" );
                    queryClient.invalidateQueries( { queryKey: getGetAllShipmentsByShopIdQueryKey( shopId || "" ) } );
                    toast.success( "Shipment updated successfully" );
                    setDialogOpen( false );
                },
                onError: ( error ) => {
                    console.error( "Error updating shipment status:", error );
                    toast.error( error.response?.data.message || error.message || "Failed to update shipment." );
                }
            } );
    };

    return (
        <Dialog open={ dialogOpen } onOpenChange={ setDialogOpen }>
            <DialogTrigger asChild>
                <Button variant={ "outline" } size={ "sm" }>
                    { status.toLowerCase() }
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        Update Order Status
                    </DialogTitle>
                    <DialogDescription>
                        Change the status of this shipment order.
                    </DialogDescription>
                </DialogHeader>
                { status === "COMPLETED" ? (
                    <p className={ "" }>
                        This order is already completed. Updating the status is not possible.
                    </p>
                ) : (
                    <Form { ...form }>
                        <form onSubmit={ form.handleSubmit( handleUpdateStatus ) } className={ "mt-4 space-y-4" }>
                            <FormField
                                control={ form.control }
                                name="status"
                                render={ ( { field } ) => (
                                    <FormItem className={ "gap-4" }>
                                        <FormLabel>Order Status</FormLabel>
                                        <FormControl>
                                            <RadioGroup
                                                onValueChange={ field.onChange }
                                                defaultValue={ field.value }
                                            >
                                                { orderStatuses
                                                    .filter( ( orderStatus ) => orderStatus.value !== status )
                                                    .map( ( status ) => (
                                                        <div key={ status.value }
                                                             className="flex items-center space-x-2">
                                                            <RadioGroupItem
                                                                value={ status.value }
                                                                id={ `status-${ status.value }` }
                                                            />
                                                            <label htmlFor={ `status-${ status.value }` }>
                                                                { status.label }
                                                            </label>
                                                        </div>
                                                    ) ) }
                                            </RadioGroup>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                ) }
                            />
                            <Button type={ "submit" }>Submit</Button>
                        </form>
                    </Form>
                ) }
            </DialogContent>
        </Dialog>
    )
}
