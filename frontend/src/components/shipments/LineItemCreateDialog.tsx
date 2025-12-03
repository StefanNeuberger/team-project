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
    status: string
}

export default function LineItemCreateDialog( { shipmentId, status }: Readonly<LineItemCreateDialogProps> ) {

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
                { status === "COMPLETED" ?
                    <p className={ "italic" }>Items cannot be added to a completed shipment</p>
                    :
                    <LineItemCreateForm closeDialog={ toggleDialog } shipmentId={ shipmentId }/>
                }
            </DialogContent>

        </Dialog>
    )
}
