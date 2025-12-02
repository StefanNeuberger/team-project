import { motion } from "framer-motion";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import EditItemForm from "@/components/ItemsPage Comps/EditItemForm.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";


export default function EditItem( { item, closeEdit }: { item: Item, closeEdit: () => void } ) {

    return (
        <motion.div
            initial={ { opacity: 0, height: 0 } }
            animate={ { opacity: 1, height: "auto" } }
            exit={ { opacity: 0, height: 0 } }
            transition={ { duration: 0.2 } }
            className={ "mt-4" }
        >
            <Card>
                <CardHeader>
                    <CardTitle>Edit Item</CardTitle>
                    <CardDescription>
                        Edit the item details below.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <EditItemForm item={ item } closeEdit={ closeEdit }/>
                </CardContent>
            </Card>
        </motion.div>
    )
}
