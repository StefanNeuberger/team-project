import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTrigger } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { DialogTitle } from "@radix-ui/react-dialog";
import CreateItemForm from "@/components/ItemsPage Comps/CreateItemForm.tsx";
import { useState } from "react";

export default function CreateItemDialog() {

    const [ open, setOpen ] = useState( false );

    const closeDialog = () => {
        setOpen( false );
    };

    return (
        <Dialog open={ open } onOpenChange={ setOpen }>
            <DialogTrigger asChild>
                <Button className={ "max-w-min ml-auto" }>
                    Create Item
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        { "Create Item" }
                    </DialogTitle>
                    <DialogDescription>
                        Create a new Item
                    </DialogDescription>
                </DialogHeader>
                <CreateItemForm closeDialog={ closeDialog }/>
            </DialogContent>
        </Dialog>
    )
}
