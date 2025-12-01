import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field.tsx";
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import type { FieldValues } from "react-hook-form";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert.tsx";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { useNavigate } from "react-router-dom";
import type { ReactNode } from "react";

type WarehouseFormProps<F extends FieldValues> = {
    form: F,
    isError: boolean,
    isSuccess: boolean,
    isPending: boolean,
    mode: "CREATE" | "UPDATE" | "DELETE",
    children?: Readonly<ReactNode>,
}

const MESSAGES = {
    ERROR: {
        CREATE: "Failed to create warehouse. Please try again.",
        UPDATE: "Failed to update warehouse. Please try again.",
        DELETE: "Failed to delete warehouse. Please try again.",
    },
    SUCCESS: {
        CREATE: "Warehouse was created successfully.",
        UPDATE: "Warehouse was updated successfully.",
        DELETE: "Warehouse was deleted successfully.",
    }
} as const;

export default function WarehouseForm<F extends FieldValues>(
    {
        form,
        mode,
        isError,
        isPending,
        isSuccess,
        children,
    }: WarehouseFormProps<F> ) {
    const navigate = useNavigate();
    const handleCancel = () => navigate( -1 );

    return (
        <>
            { mode !== "DELETE" ?
                <>
                    <FieldGroup>
                        <FieldSet>
                            <FieldLegend>Basic</FieldLegend>
                            <FieldDescription>Basic information about the warehouse</FieldDescription>
                            <FieldGroup>
                                <div className="grid grid-cols-2 gap-4">
                                    <FormField
                                        control={ form.control }
                                        name="name"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Warehouse Name</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="Type in a name..." { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormDescription>
                                                    This field is required and must be a unique name
                                                </FormDescription>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="maxCapacity"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Maximum Capacity</FormLabel>
                                                <FormControl>
                                                    <Input type="number"
                                                           placeholder="Type in a number..." { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormDescription>
                                                    This field is required
                                                </FormDescription>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                        </FieldSet>
                    </FieldGroup>
                    <FieldGroup>
                        <FieldSet>
                            <FieldLegend>Address</FieldLegend>
                            <FieldDescription>Address of the warehouse</FieldDescription>
                            <FieldGroup>
                                <div className="grid grid-cols-2 gap-4">
                                    <FormField
                                        control={ form.control }
                                        name="street"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Street</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. Berlinerstraße" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="number"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Number</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. 210 a-b" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                            <FieldGroup>
                                <div className="grid grid-cols-2 gap-4">
                                    <FormField
                                        control={ form.control }
                                        name="postalCode"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Postal Code</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. 12345" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="city"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>City</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. Berlin" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                            <FieldGroup>
                                <div className="grid grid-cols-2 gap-4">
                                    <FormField
                                        control={ form.control }
                                        name="country"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>State</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. Berlin" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="state"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Country</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="e.g. Germany" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                        </FieldSet>
                    </FieldGroup>
                    <FieldGroup>
                        <FieldSet>
                            <FieldLegend>Geo Location</FieldLegend>
                            <FieldDescription>This location will be used on maps</FieldDescription>
                            <FieldGroup>
                                <div className="grid grid-cols-2 gap-4">
                                    <FormField
                                        control={ form.control }
                                        name="lng"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Longitude </FormLabel>
                                                <FormControl>
                                                    <Input type="number"  { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="lat"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Latitude</FormLabel>
                                                <FormControl>
                                                    <Input type="number" { ...field }
                                                           disabled={ isPending }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                        </FieldSet>
                    </FieldGroup>
                </>
                :
                <>
                    { children }
                </>
            }
            { isError && !form.formState.errors.name && (
                <FieldGroup>
                    <Alert variant="destructive">
                        <AlertCircleIcon/>
                        <AlertTitle>Oops! We encountered an error.</AlertTitle>
                        <AlertDescription>
                            { MESSAGES.ERROR[ mode ] }
                        </AlertDescription>
                    </Alert>
                </FieldGroup>
            ) }
            { isSuccess && (
                <FieldGroup>
                    <Alert variant="default">
                        <CheckCircle2Icon/>
                        <AlertTitle>
                            { MESSAGES.SUCCESS[ mode ] }
                        </AlertTitle>
                    </Alert>
                </FieldGroup>
            ) }
            { mode !== "DELETE" ?
                <FieldGroup>
                    <Button type="submit" variant="default" disabled={ isPending }>
                        { isPending ?
                            <>
                                { "Saving" }
                                <Spinner/>
                            </> : "Save" }
                    </Button>
                    <Button type="button" variant="secondary" onClick={ handleCancel }
                            disabled={ isPending }>
                        { isSuccess ? "Go Back " : "Cancel" }
                    </Button>
                </FieldGroup>
                :
                <FieldGroup className="flex flex-row items-center justify-center p-0!">
                    <Button type="submit" variant="destructive" className="basis-1/2"
                            disabled={ isPending || isSuccess }>
                        { isPending ?
                            <>
                                { "Deleting" }
                                <Spinner/>
                            </> : "Delete" }
                    </Button>
                    <Button type="button" variant="default" onClick={ handleCancel }
                            disabled={ isPending || isSuccess }
                            className="basis-1/2">Cancel</Button>
                </FieldGroup>
            }

        </>
    )
}