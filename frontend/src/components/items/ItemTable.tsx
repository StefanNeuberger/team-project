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

type ItemTableProps = {
    shopId: string;
    data: InventoryResponseDTO[]
}


export default function ItemTable( { shopId, data }: ItemTableProps ) {
    const navigate = useNavigate();

    const columns: ColumnDef<InventoryResponseDTO>[] = useMemo( () => [
        {
            accessorKey: "warehouse",
            header: "Warehouse",
            cell: ( { row } ) => {
                return <Link className="hover:underline"
                             to={ `/shop/${ shopId }/warehouses/${ row.original.warehouse.id }` }><span
                    className="font-bold">{ row.original.warehouse.name }</span></Link>
            }
        },
        {
            accessorKey: "address",
            header: "Address",
            cell: ( { row } ) => {
                const item = row.original;
                if ( !item.warehouse.address || !item.warehouse.geoLocation ) return "-";
                const address = Object.values( item.warehouse.address ).join( ", " );
                if ( !item.warehouse.geoLocation ) return address;
                return <a className="hover:underline" target="_blank"
                          href={ `https://www.google.com/maps/search/?api=1&query=${ item.warehouse.geoLocation[ 1 ] }%2C${ item.warehouse.geoLocation[ 0 ] }` }>
                    { address }
                </a>
            }
        },
        {
            accessorKey: "maxCapacity",
            header: "Max capacity",
            cell: ( { row } ) => {
                return new Intl.NumberFormat( "de", {} ).format( row.original.warehouse.maxCapacity ) + " Units";
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
                                onClick={ () => navigate( `/shop/${ shopId }/warehouses/${ inventory.warehouse.id }` ) }
                            >
                                Details
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )
            }
        },
    ], [ navigate, shopId ] );


    return <DataTable columns={ columns } data={ data } key="id"></DataTable>
}