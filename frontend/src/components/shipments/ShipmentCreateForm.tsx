import { z } from "zod";
import { ShipmentCreateDTOStatus } from "@/api/generated/openAPIDefinition.schemas.ts";
import { useForm } from "react-hook-form";
import { Form } from "@/components/ui/form.tsx";
import { zodResolver } from "@hookform/resolvers/zod";
import ShipmentForm from "@/components/shipments/ShipmentForm.tsx";
import { getGetAllShipmentsByShopIdQueryKey, useCreateShipment } from "@/api/generated/shipments/shipments.ts";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useParams } from "react-router-dom";

type ShipmentCreateFormProps = {
    closeDialog: () => void;
}

type ShipmentFormData = {
    warehouseId: string;
    expectedArrivalDate: string;
    status: ShipmentCreateDTOStatus;
}

const ShipmentSchema = z.object( {
        warehouseId: z.string().min( 1, 'Warehouse ID is required' ),
        expectedArrivalDate: z.string().min( 1, 'Expected arrival date is required' )
            .transform( ( dateString ) => {
                // Convert "YYYY-MM-DD" to ISO-8601 String
                return new Date( dateString + 'T00:00:00.000Z' ).toISOString();
            } ),
        status: z.enum( [
            ShipmentCreateDTOStatus.ORDERED,
            ShipmentCreateDTOStatus.PROCESSED,
            ShipmentCreateDTOStatus.IN_DELIVERY,
            ShipmentCreateDTOStatus.COMPLETED
        ], {
            message: 'Status is required and must be a valid value'
        } )
    }
);


export default function ShipmentCreateForm( { closeDialog }: Readonly<ShipmentCreateFormProps> ) {

    const { shopId } = useParams();
    const createShipment = useCreateShipment();
    const queryClient = useQueryClient();


    const form = useForm<ShipmentFormData>( {
        resolver: zodResolver( ShipmentSchema ),
        defaultValues: {
            warehouseId: "",
            expectedArrivalDate: "",
            status: ShipmentCreateDTOStatus.ORDERED
        }
    } );

    const handleCreateShipment = ( data: ShipmentFormData ) => {
        // Implement shipment creation logic here
        console.log( "Created shipment" );
        createShipment.mutate(
            {
                data: {
                    warehouseId: data.warehouseId,
                    expectedArrivalDate: data.expectedArrivalDate,
                    status: data.status
                }
            },
            {
                onSuccess: () => {
                    // Invalidate and refetch shipments list
                    queryClient.invalidateQueries( { queryKey: getGetAllShipmentsByShopIdQueryKey( shopId || "" ) } );
                    toast.success( "Shipment created successfully." );
                    closeDialog();
                },
                onError: ( error ) => {
                    console.error( "Error creating shipment:", error );
                    toast.error( error.response?.data.message || error.message || "Failed to create shipment." );
                }
            }
        )
    }

    return (
        <Form { ...form }>
            <form onSubmit={ form.handleSubmit( handleCreateShipment ) }>
                <ShipmentForm form={ form }/>
            </form>
        </Form>
    )
}
