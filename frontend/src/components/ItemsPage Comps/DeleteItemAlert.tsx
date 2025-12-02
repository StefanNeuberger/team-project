import { motion } from "framer-motion";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert.tsx";
import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type DeleteItemAlertProps = {
    isPending: boolean,
    onDeleteItem: () => void,
    toggleShowConfirmDelete: () => void
}

export default function DeleteItemAlert( {
                                             isPending,
                                             onDeleteItem,
                                             toggleShowConfirmDelete
                                         }: DeleteItemAlertProps ) {

    return (

        <motion.div
            initial={ { opacity: 0, height: 0 } }
            animate={ { opacity: 1, height: "auto" } }
            exit={ { opacity: 0, height: 0 } }
            transition={ { duration: 0.2 } }
            className={ "mt-4" }
        >
            <Alert variant={ "destructive" }>
                <Trash2/>
                <AlertTitle>
                    Are you sure you want to delete this item?
                </AlertTitle>
                <AlertDescription>
                    <p>This actions cannot be undone</p>
                    <div className={ "flex justify-between mt-4 w-full items-center" }>
                        <Button variant={ "destructive" } disabled={ isPending }
                                onClick={ onDeleteItem }>
                            Delete Item
                        </Button>
                        <Button variant={ "outline" } onClick={ toggleShowConfirmDelete }>
                            Cancel
                        </Button>
                    </div>
                </AlertDescription>
            </Alert>
        </motion.div>

    )
}
