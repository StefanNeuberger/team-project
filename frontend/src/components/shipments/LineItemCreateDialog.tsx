import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import LineItemCreateForm from "@/components/shipments/LineItemCreateForm.tsx";
import { useState } from "react";

type LineItemCreateDialogProps = {
    shipmentId: string;
}

export default function LineItemCreateDialog( { shipmentId }: Readonly<LineItemCreateDialogProps> ) {

    const [ open, setOpen ] = useState( false );

    const toggleDialog = () => {
        setOpen( !open );
    };

    return (
        <Dialog open={ open } onOpenChange={ toggleDialog }>
            <DialogTrigger asChild>
                <Button variant={ "link" }>
                    Add Item
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        Add Line Item
                    </DialogTitle>
                    <DialogDescription>
                        Add a new line item to this shipment.
                    </DialogDescription>
                </DialogHeader>
                <LineItemCreateForm closeDialog={ toggleDialog } shipmentId={ shipmentId }/>
            </DialogContent>

        </Dialog>
    )
}
