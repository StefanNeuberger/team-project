import { useState } from "react";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTrigger } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { DialogTitle } from "@radix-ui/react-dialog";
import ShipmentCreateForm from "@/components/shipments/ShipmentCreateForm.tsx";

export default function ShipmentCreateDialog() {

    const [ open, setOpen ] = useState( false );

    const closeDialog = () => {
        setOpen( false );
    };

    return (
        <Dialog open={ open } onOpenChange={ setOpen }>
            <DialogTrigger asChild>
                <Button variant={ "link" } className={ "max-w-min ml-auto" } size={ "sm" }>
                    Add Shipment
                </Button>
            </DialogTrigger>
            <DialogContent className={ "max-h-[90vh] overflow-y-scroll" }>
                <DialogHeader>
                    <DialogTitle>
                        { "Create Shipment" }
                    </DialogTitle>
                    <DialogDescription>
                        Create a new Shipment by filling out the form below.
                    </DialogDescription>
                </DialogHeader>
                <ShipmentCreateForm closeDialog={ closeDialog }/>
            </DialogContent>
        </Dialog>
    )
}
