import { type ColumnDef } from "@tanstack/react-table"
import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import { DataTable } from "@/components/custom-ui/DataTable.tsx";
import {
    DropdownMenu,
    DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button.tsx";
import { MoreHorizontal } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useMemo } from "react";

type WarehouseTableProps = {
    shopId: string;
    data: WarehouseResponseDTO[]
}


export default function WarehouseTable( { shopId, data }: WarehouseTableProps ) {
    const navigate = useNavigate();

    const columns: ColumnDef<WarehouseResponseDTO>[] = useMemo( () => [
        {
            accessorKey: "name",
            header: "Name",
            cell: ( { row } ) => {
                return <Link className="hover:underline"
                             to={ `/shop/${ shopId }/warehouses/${ row.original.id }` }><span
                    className="font-bold">{ row.original.name }</span></Link>
            }
        },
        {
            accessorKey: "address",
            header: "Address",
            cell: ( { row } ) => {
                const warehouse = row.original;
                if ( !warehouse.address || !warehouse.geoLocation ) return "-";
                const address = Object.values( warehouse.address ).join( ", " );
                if ( !warehouse.geoLocation ) return address;
                return <a className="hover:underline" target="_blank"
                          href={ `https://www.google.com/maps/search/?api=1&query=${ warehouse.geoLocation[ 1 ] }%2C${ warehouse.geoLocation[ 0 ] }` }>
                    { address }
                </a>
            }
        },
        {
            accessorKey: "maxCapacity",
            header: "Max capacity",
            cell: ( { row } ) => {
                return new Intl.NumberFormat( "de", {} ).format( row.original.maxCapacity ) + " Units";
            }
        },
        {
            id: "actions",
            cell: ( { row } ) => {
                const warehouseResponseDTO = row.original;

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
                                onClick={ () => navigate( `/shop/${ shopId }/warehouses/${ warehouseResponseDTO.id }` ) }
                            >
                                Details
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={ () => navigate( `/shop/${ shopId }/warehouses/${ warehouseResponseDTO.id }/edit` ) }>
                                Edit
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem
                                onClick={ () => navigate( `/shop/${ shopId }/warehouses/${ warehouseResponseDTO.id }/delete` ) }>
                                Delete
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )
            }
        },
    ], [ navigate, shopId ] );


    const warehouses = data
        .filter( warehouse => warehouse.shop.id === shopId );


    return <DataTable columns={ columns } data={ warehouses } key="id"></DataTable>
}