import { Button } from "@/components/ui/button.tsx";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.tsx";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { useGetAllWarehouses } from "@/api/generated/warehouses/warehouses.ts";
import type { ShipmentCreateDTOStatus } from "@/api/generated/openAPIDefinition.schemas.ts";
import type { UseFormReturn } from "react-hook-form";

type ShipmentFormData = {
    warehouseId: string;
    expectedArrivalDate: string;
    status: ShipmentCreateDTOStatus;
}

type ShipmentFormProps = {
    form: UseFormReturn<ShipmentFormData>;
}

export default function ShipmentForm( { form }: Readonly<ShipmentFormProps> ) {
    const { data: warehousesData, isLoading: warehousesLoading } = useGetAllWarehouses();

    const orderStatuses = [
        { label: "Ordered", value: "ORDERED" },
        { label: "Processed", value: "PROCESSED" },
        { label: "In Delivery", value: "IN_DELIVERY" }
    ] as { label: string; value: ShipmentCreateDTOStatus }[];

    if ( warehousesLoading ) {
        return <div><Spinner/></div>;
    }

    return (
        <div className="space-y-8 my-4">
            <FormField
                control={ form.control }
                name="warehouseId"
                render={ ( { field } ) => (
                    <FormItem>
                        <FormLabel>Select a Warehouse</FormLabel>
                        <Select onValueChange={ field.onChange } defaultValue={ field.value }>
                            <FormControl>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select a warehouse"/>
                                </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                                { warehousesData?.data.map( ( warehouse ) => (
                                    <SelectItem key={ warehouse.id } value={ warehouse.id }>
                                        { warehouse.name }
                                    </SelectItem>
                                ) ) }
                            </SelectContent>
                        </Select>
                        <FormMessage/>
                    </FormItem>
                ) }
            />

            <FormField
                control={ form.control }
                name="expectedArrivalDate"
                render={ ( { field } ) => (
                    <FormItem>
                        <FormLabel>Expected Arrival Date</FormLabel>
                        <FormControl>
                            <Input type="date" { ...field } />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                ) }
            />

            <FormField
                control={ form.control }
                name="status"
                render={ ( { field } ) => (
                    <FormItem>
                        <FormLabel>Order Status</FormLabel>
                        <FormControl>
                            <RadioGroup
                                onValueChange={ field.onChange }
                                defaultValue={ field.value }
                            >
                                { orderStatuses.map( ( status ) => (
                                    <div key={ status.value } className="flex items-center space-x-2">
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

            <Button type="submit">Submit</Button>
        </div>
    );
}
