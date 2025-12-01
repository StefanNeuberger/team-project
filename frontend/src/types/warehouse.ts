import { z } from "zod";

export const warehouseFormSchema = z.object( {
    name: z.string()
        .min( 1, "Warehouse name is required" )
        .min( 2, "Warehouse name must be at last 2 characters" )
        .max( 100, "Warehouse name must be less than 100 characters" )
        .trim(),
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

export type WarehouseFormData = z.infer<typeof warehouseFormSchema>;