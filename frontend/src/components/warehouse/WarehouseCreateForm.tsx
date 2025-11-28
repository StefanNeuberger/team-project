import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle
} from "@/components/ui/card.tsx";
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage
} from "@/components/ui/form";
import { Button } from "@/components/ui/button.tsx";
import {
    getGetAllWarehousesQueryKey,
    usePostWarehouse
} from "@/api/generated/warehouses/warehouses.ts";
import { Alert, AlertTitle, AlertDescription } from "@/components/ui/alert";
import { z } from "zod";
import { useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input.tsx";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { useNavigate } from "react-router-dom";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";

const warehouseFormSchema = z.object( {
    name: z.string()
        .min( 1, "Warehouse name is required" )
        .min( 2, "Warehouse name must be at last 2 characters" )
        .max( 100, "Warehouse name must be less than 100 characters" )
        .trim(),
    // maxCapacity: z.string().refine( ( val ) => Number( val ) >= 1 ),
    maxCapacity: z.coerce.number<number>()
        .positive( "Warehouse max capacity cannot be negative" )
        .min( 1, "Warehouse max capacity is required" ),
    street: z.string(),
    number: z.string(),
    city: z.string(),
    postalCode: z.string(),
    state: z.string(),
    country: z.string(),
    lat: z.coerce.number<number>(),
    lng: z.coerce.number<number>()
} )

type WarehouseFormData = z.infer<typeof warehouseFormSchema>;

type WarehouseCreateFormProps = {
    shopId: string;
}

export default function WarehouseCreateForm( { shopId }: WarehouseCreateFormProps ) {
    const navigate = useNavigate();
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
                onSuccess: ( data ) => {
                    console.log( data );
                    queryClient.invalidateQueries( {
                        queryKey: getGetAllWarehousesQueryKey()
                    } )
                },
                onError: ( error ) => {
                    // if ( error?.response?.status === 409 ) {
                    //     console.error( "Failed to create warehouse:", error );
                    //     form.setError( "name", {
                    //         type: "manual",
                    //         message: error.response.data.message
                    //     } );
                    // } else {
                    // For other errors, show generic error
                    console.error( "Failed to create warehouse:", error );
                    // }
                }
            }
        )
    }

    const handleCancel = () => navigate( -1 );


    return <Card className="w-full">
        <CardHeader>
            <CardTitle>Create Warehouse</CardTitle>
            <CardDescription>Fill out the fields and click save to create a new warehouse.</CardDescription>
        </CardHeader>
        <CardContent>
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleEditWarehouse ) } className="space-y-8">
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
                                                </FormControl>
                                                <FormDescription>
                                                    This field is required and must be a unqique name
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
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
                                        name="lat"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Latitude</FormLabel>
                                                <FormControl>
                                                    <Input type="number" { ...field }
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                    <FormField
                                        control={ form.control }
                                        name="lng"
                                        render={ ( { field } ) => (
                                            <FormItem>
                                                <FormLabel>Longitude </FormLabel>
                                                <FormControl>
                                                    <Input type="number"  { ...field }
                                                           disabled={ postWarehouse.isPending || postWarehouse.isSuccess }/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        ) }
                                    />
                                </div>
                            </FieldGroup>
                        </FieldSet>
                    </FieldGroup>
                    { postWarehouse.isError && !form.formState.errors.name && (
                        <FieldGroup>
                            <Alert variant="destructive">
                                <AlertCircleIcon/>
                                <AlertTitle>Oops! We encountered an error.</AlertTitle>
                                <AlertDescription>
                                    Failed to create warehouse. Please try again.
                                </AlertDescription>
                            </Alert>
                        </FieldGroup>
                    ) }
                    { postWarehouse.isSuccess && (
                        <FieldGroup>
                            <Alert variant="default">
                                <CheckCircle2Icon/>
                                <AlertTitle>Success! Warehouse has been created</AlertTitle>
                            </Alert>
                        </FieldGroup>
                    ) }
                    <FieldGroup>
                        <Button type="submit" variant="default"
                                disabled={ postWarehouse.isPending || postWarehouse.isSuccess }>
                            { postWarehouse.isPending ?
                                <>
                                    { "Creating" }
                                    <Spinner/>
                                </> : "Create" }
                        </Button>
                        <Button type="button" variant="secondary" onClick={ handleCancel }
                                disabled={ postWarehouse.isPending }>Cancel</Button>
                    </FieldGroup>
                </form>
            </Form>
        </CardContent>
    </Card>
}