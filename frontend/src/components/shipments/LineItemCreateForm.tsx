import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form } from "@/components/ui/form.tsx";
import LineItemForm from "@/components/shipments/LineItemForm.tsx";
import {
    getGetAllShipmentLineItemsByShipmentIdQueryKey,
    useCreateShipmentLineItem
} from "@/api/generated/shipment-line-items/shipment-line-items.ts";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";


type LineItemCreateFormProps = {
    closeDialog: () => void;
    shipmentId: string;
}

type LineItemFormData = {
    itemId: string;
    expectedQuantity: number;
    receivedQuantity: number;
}

export const LineItemSchema = z.object( {
    itemId: z.string().min( 1, "Item ID is required" ),
    expectedQuantity: z.number().min( 0, "Must be at least 0" ),
    receivedQuantity: z.number().min( 0, "Must be at least 0" ),
} );

export default function LineItemCreateForm( { closeDialog, shipmentId }: Readonly<LineItemCreateFormProps> ) {

    const createLineItem = useCreateShipmentLineItem();
    const queryClient = useQueryClient();

    const form = useForm<LineItemFormData>( {
        resolver: zodResolver( LineItemSchema ),
        defaultValues: {
            expectedQuantity: 0,
            receivedQuantity: 0,
            itemId: ""
        }
    } );

    const handleCreateLineItem = ( data: LineItemFormData ) => {
        // Implement line item creation logic here
        console.log( "Created line item", { ...data, shipmentId } );
        createLineItem.mutate(
            {
                data: {
                    ...data,
                    shipmentId: shipmentId
                }
            },
            {
                onSuccess: () => {
                    queryClient.invalidateQueries( { queryKey: getGetAllShipmentLineItemsByShipmentIdQueryKey( shipmentId || "" ) } );
                    toast.success( "Line item created successfully" );
                    closeDialog();
                },
                onError: ( error ) => {
                    console.error( "Error creating line item: ", error );
                    toast.error( error.response?.data.message || error.message || "Failed to add item." );
                }
            }
        );
    }

    return (
        <Form { ...form }>
            <form
                onSubmit={ form.handleSubmit( handleCreateLineItem ) }>
                <LineItemForm form={ form }/>
            </form>

        </Form>
    )
}
