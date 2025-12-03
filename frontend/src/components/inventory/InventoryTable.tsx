import { type ColumnDef } from "@tanstack/react-table"
import type { InventoryResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { DataTable } from "@/components/custom-ui/DataTable.tsx";
import {
    DropdownMenu,
    DropdownMenuContent, DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button.tsx";
import { MoreHorizontal } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useMemo } from "react";

type InventoryTableProps = {
    shopId: string;
    data: InventoryResponseDTO[],
    showWarehouse?: boolean;
}


export default function InventoryTable( { shopId, data, showWarehouse = false }: InventoryTableProps ) {
    const navigate = useNavigate();

    const columns: ColumnDef<InventoryResponseDTO>[] = useMemo( () => {
        const initialColumns: ColumnDef<InventoryResponseDTO>[] = [
            {
                accessorKey: "item",
                header: "Item Name",
                cell: ( { row } ) => {
                    return <Link className="hover:underline"
                                 to={ `/shop/${ shopId }/items/${ row.original.item.id }` }><span
                        className="font-bold">{ row.original.item.name }</span></Link>
                }
            },
            {
                accessorKey: "sku",
                header: "Item SKU",
                cell: ( { row } ) => {
                    return row.original.item.sku;
                }
            },
            {
                accessorKey: "quantity",
                header: "Quantity"
            },
            {
                id: "actions",
                cell: ( { row } ) => {
                    const inventory = row.original;

                    return (
                        <DropdownMenu dir="ltr">
                            <DropdownMenuTrigger asChild>
                                <Button variant="ghost" className="h-8 w-8 p-0">
                                    <span className="sr-only">Open menu</span>
                                    <MoreHorizontal className="h-4 w-4"/>
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="start">
                                <DropdownMenuItem
                                    onClick={ () => navigate( `/shop/${ shopId }/inventory/${ inventory.id }` ) }
                                >
                                    Details
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    )
                }
            },
        ]
        if ( showWarehouse ) {
            return [
                {
                    accessorKey: "warehouse",
                    header: "Warehouse Name",
                    cell: ( { row } ) => {
                        return <Link className="hover:underline"
                                     to={ `/shop/${ shopId }/inventory/${ row.original.id }` }><span
                            className="font-bold">{ row.original.warehouse.name }</span></Link>
                    }
                },
                ...initialColumns,
            ]
        }
        return initialColumns;
    }, [ navigate, shopId, showWarehouse ] );


    return <DataTable columns={ columns } data={ data } key="id"></DataTable>
}