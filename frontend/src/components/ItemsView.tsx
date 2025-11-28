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

export default function ItemsView( { items }: Readonly<{ items: Item[] | undefined }> ) {

    console.log( items );
    return (
        <div className={ "w-full p-4" }>
            <Table>
                <TableCaption>All Items</TableCaption>
                <TableHeader>
                    <TableRow>
                        <TableHead className="w-[100px]">Nr.</TableHead>
                        <TableHead>Name</TableHead>
                        <TableHead>SKU</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    { items?.map( ( invoice, index ) => (
                        <TableRow key={ invoice.id }>
                            <TableCell className="font-medium">{ index + 1 }</TableCell>
                            <TableCell>{ invoice.name }</TableCell>
                            <TableCell>{ invoice.sku }</TableCell>
                        </TableRow>
                    ) ) }
                </TableBody>
                <TableFooter>
                    <TableRow></TableRow>
                </TableFooter>
            </Table>
        </div>
    )
}
