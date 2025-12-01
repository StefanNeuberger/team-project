import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableFooter,
    TableHead,
    TableHeader,
    TableRow
} from "@/components/ui/table.tsx";
import { motion } from "framer-motion";
import { Spinner } from "@/components/ui/spinner.tsx";
import type { ItemsViewProps, ItemViewItem } from "@/components/ItemsPage Comps/ItemsView.tsx";
import ItemDialogDetailsView from "@/components/ItemsPage Comps/ItemDialogDetailsView.tsx";


export default function ItemsDefaultView( {
                                              items,
                                              quantities,
                                              quantityLoading
                                          }: Readonly<ItemsViewProps> ) {

    return (
        <div className={ "m-auto" }>
            <small>Total Items: { items?.length ?? "N/A" }</small>
            <Table className={ "my-4 hidden md:block" }>
                { items?.length > 0 && <TableCaption>All Items</TableCaption> }
                <TableHeader>
                    <TableRow className={ "border-muted-foreground" }>
                        <TableHead className={ "w-[100px]" }>Nr.</TableHead>
                        <TableHead className={ "w-screen max-w-[600px]" }>Name</TableHead>
                        <TableHead className={ "w-[100px]" }>SKU</TableHead>
                        <TableHead className={ "w-[100px]" }>Total #</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    { items?.map( ( item: ItemViewItem, index ) => (
                        <motion.tr
                            className={ "border-b-[0.5px] border-muted-foreground hover:bg-muted" }
                            key={ item.id }
                            initial={ { opacity: 0, x: -20 } }
                            animate={ { opacity: 1, x: 0 } }
                        >
                            <TableCell
                                className={ "font-medium py-3 text-muted-foreground" }>{ index + 1 }
                            </TableCell>
                            <TableCell
                                className={ " flex justify-between items-center " }>{ item.name }
                                <ItemDialogDetailsView item={ item }
                                                       itemQuantity={ quantities?.[ item.id ] }/>
                            </TableCell>
                            <TableCell
                                className={ "font-medium text-muted-foreground" }>{ item.sku
                            }</TableCell>
                            <TableCell
                                className={ "font-medium text-muted-foreground" }>{
                                quantityLoading ?
                                    <Spinner/> : quantities?.[ item.id ]?.totalQuantity ?? "n/a"
                            }
                            </TableCell>
                        </motion.tr>
                    ) ) }
                </TableBody>
                <TableFooter className={ "border-0" }>
                    <TableRow></TableRow>
                </TableFooter>
            </Table>
        </div>
    )
}
