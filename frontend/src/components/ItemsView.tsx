import { type Item } from '../api/generated/openAPIDefinition.schemas';
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
import ItemsSearchBar from "@/components/ItemsSearchBar.tsx";
import { useState } from "react";
import { motion } from "framer-motion";
import { Spinner } from "@/components/ui/spinner.tsx";

type ItemsViewProps = {
    items: ItemViewItem[],
    quantities: Record<string, number>,
    quantityLoading?: boolean,
}

type ItemViewItem = Item & {
    quantity?: number;

}
export default function ItemsView( { items, quantities, quantityLoading }: Readonly<ItemsViewProps> ) {

    const [ searchQuery, setSearchQuery ] = useState<string>( "" );
    const [ filteredItems, setFilteredItems ] = useState<Item[] | []>( items || [] );

    console.log( items[ 0 ] )

    const handleSearchInputChange = ( query: string ) => {
        setSearchQuery( query );

        setFilteredItems( items.filter( ( item ) => {
            const lowerCaseQuery = query.toLowerCase();
            const skuCases = item.sku ? [ item.sku.toLowerCase(), item.sku.replaceAll( "-", " " ).toLowerCase(), item.sku.replaceAll( "-", "" ).toLowerCase() ] : [];

            return item.name.toLowerCase().includes( lowerCaseQuery ) || skuCases.some( sku => sku.includes( lowerCaseQuery ) );
        } ) );
    };


    console.log( quantities );

    return (
        <div className={ "w-full flex flex-col px-8 mb-8" }>
            <ItemsSearchBar onInputChange={ handleSearchInputChange } inputValue={ searchQuery }/>
            <small>Total Items: { filteredItems?.length ?? "N/A" }</small>
            <Table className={ "my-4" }>
                { filteredItems?.length > 0 && <TableCaption>All Items</TableCaption> }
                <TableHeader>
                    <TableRow className={ "border-muted-foreground" }>
                        <TableHead className={ "w-[100px]" }>Nr.</TableHead>
                        <TableHead>Name</TableHead>
                        <TableHead>SKU</TableHead>
                        <TableHead>Total #</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    { filteredItems?.map( ( item: ItemViewItem, index ) => (
                        <motion.tr
                            className={ "border-b-[0.5px] border-muted-foreground hover:bg-muted" }
                            key={ item.id }
                            initial={ { opacity: 0, x: -20 } }
                            animate={ { opacity: 1, x: 0 } }
                        >
                            <TableCell
                                className={ "font-medium py-3 text-muted-foreground" }>{ index + 1 }
                            </TableCell>
                            <TableCell className={ "" }>{ item.name }</TableCell>
                            <TableCell
                                className={ "font-medium text-muted-foreground" }>{ item.sku
                            }</TableCell>
                            <TableCell
                                className={ "font-medium text-muted-foreground" }>{
                                quantityLoading ?
                                    <Spinner/> : quantities[ item.id ] ?? "n/a"
                            }
                            </TableCell>
                        </motion.tr>
                    ) ) }
                </TableBody>
                <TableFooter className={ "border-0" }>
                    <TableRow></TableRow>
                </TableFooter>
            </Table>
            { filteredItems?.length === 0 &&
                <p className={ "m-auto my-8 text-accent-foreground" }>No Items found</p>
            }
        </div>
    )
}
