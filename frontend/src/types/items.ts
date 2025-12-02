import { z } from "zod";

export const itemFormSchema = z.object( {
    name: z.string()
        .min( 1, "Item name is required" )
        .min( 2, "Item name must be at last 2 characters" )
        .max( 100, "Item name must be less than 100 characters" )
        .trim(),
    sku: z.string()
        .min( 1, "SKU is required" )
        .regex( /^[A-Z]{2}-[A-Z]{3}-\d{2}$/, "SKU must follow format XX-XXX-XX (e.g., KL-MED-15)" )
        .trim(),
} );

export type ItemFormData = {
    name: string;
    sku: string;
}