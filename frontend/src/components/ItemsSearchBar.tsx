import { Field, FieldDescription, FieldLabel } from "@/components/ui/field.tsx";
import { Input } from "@/components/ui/input.tsx";
import React from "react";

export default function ItemsSearchBar( { onInputChange, inputValue }: Readonly<{
    onInputChange: ( value: string ) => void;
    inputValue: string
}> ) {

    const handleChange = ( event: React.ChangeEvent<HTMLInputElement> ) => {
        onInputChange( event.target.value );
    };

    return (
        <div className={ "w-full flex justify-center items-center flex-col p-4" }>
            <Field className={ "w-1/2 max-w-[500px]" }>
                <FieldLabel>
                    Search Items:
                </FieldLabel>
                <Input type={ "text" } onChange={ handleChange } value={ inputValue }
                       placeholder={ "Search Name or Sku..." }/>
                <FieldDescription>
                    Look up an item by its name or SKU.
                </FieldDescription>
            </Field>
        </div>
    )
}
