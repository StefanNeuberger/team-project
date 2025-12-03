import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import type { ShipmentLineItemResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import {
    getGetAllShipmentLineItemsByShipmentIdQueryKey,
    useUpdateShipmentLineItem
} from "@/api/generated/shipment-line-items/shipment-line-items.ts";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { Spinner } from "@/components/ui/spinner.tsx";

type LineItemUpdateFormProps = {
    lineItem: ShipmentLineItemResponseDTO
    shipmentId: string;
    closeEditMode: () => void;
}

type LineUpdateFormData = {
    expectedQuantity: number;
    receivedQuantity: number;
}

const LineUpdateFormSchema = z.object( {
    expectedQuantity: z.number().min( 0, "Expected quantity must be at least 0" ),
    receivedQuantity: z.number().min( 0, "Received quantity must be at least 0" ),
} )

export default function LineItemUpdateForm( {
                                                lineItem,
                                                closeEditMode,
                                                shipmentId
                                            }: Readonly<LineItemUpdateFormProps> ) {

    const updateLineItem = useUpdateShipmentLineItem();
    const queryClient = useQueryClient();

    const form = useForm<LineUpdateFormData>(
        {
            resolver: zodResolver( LineUpdateFormSchema ),
            defaultValues: {
                expectedQuantity: lineItem.expectedQuantity,
                receivedQuantity: lineItem.receivedQuantity
            }
        }
    );

    const handleUpdateLineItem = ( data: LineUpdateFormData ) => {
        updateLineItem.mutate(
            {
                id: lineItem.id || "",
                data: {
                    expectedQuantity: data.expectedQuantity,
                    receivedQuantity: data.receivedQuantity
                }
            },
            {
                onSuccess: () => {
                    console.log( "Line item updated successfully" );
                    toast.success( "LineItem updated successfully" );
                    queryClient.invalidateQueries( {
                        queryKey: getGetAllShipmentLineItemsByShipmentIdQueryKey( shipmentId || "" )
                    } );
                    closeEditMode();
                },
                onError: ( error ) => {
                    console.error( "Error updating line item", error );
                    toast.error( error.response?.data.message || error.message || "Failed to add item." );
                }
            }
        );
    }

    const updateLineItemPending = updateLineItem.isPending;

    const disableSubmitButton = updateLineItemPending || !form.formState.isDirty || !form.formState.isValid;


    return (

        <Form { ...form }>
            <form onSubmit={ form.handleSubmit( handleUpdateLineItem ) } className={ "space-y-4" }>
                <FormField
                    name={ "expectedQuantity" }
                    control={ form.control }
                    render={ ( { field } ) => (
                        <FormItem>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Expected Quantity</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input placeholder={ "Expected quantity" }
                                       type={ "number" }
                                       { ...field }
                                       onChange={ ( e ) => field.onChange( Number( e.target.value ) ) }
                                />
                            </FormControl>
                        </FormItem>
                    ) }
                />
                <FormField
                    name={ "receivedQuantity" }
                    control={ form.control }
                    render={ ( { field } ) => (
                        <FormItem>
                            <div className={ "flex items-center justify-between" }>
                                <FormLabel>Received Quantity</FormLabel>
                                <FormMessage className={ "text-xs" }/>
                            </div>
                            <FormControl>
                                <Input placeholder={ "Expected quantity" }
                                       type={ "number" }
                                       { ...field }
                                       onChange={ ( e ) => field.onChange( Number( e.target.value ) ) }
                                />
                            </FormControl>
                        </FormItem>
                    ) }
                />
                <div className={ "flex items-center justify-between" }>
                    <Button disabled={ disableSubmitButton }
                            className={ "flex justify-center items-center" }
                            size={ "sm" }
                            type={ "submit" }>
                        <p className={ `${ updateLineItemPending ? "invisible" : "" }` }>
                            Submit
                        </p>
                        { updateLineItemPending && <Spinner className={ "absolute" }/> }
                    </Button>
                    <Button variant={ "outline" } size={ "sm" } onClick={ closeEditMode }>Cancel</Button>
                </div>
            </form>

        </Form>


    )
}
