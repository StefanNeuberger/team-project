import { useGetAllItems } from "@/api/generated/items/items.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import ItemsView from "@/components/ItemsView.tsx";
import { useGetAll1 } from "@/api/generated/inventory/inventory.ts";
import { useEffect, useState } from "react";

export default function ItemsPage() {

    const { data: itemsData, isLoading, isFetched: itemsFetched } = useGetAllItems();

    const { data: inventoryData, isFetched: inventoriesFetched } = useGetAll1();

    const [ quantityLoading, setQuantityLoading ] = useState<boolean>( true );

    const [ itemsWithQuantities, setItemsWithQuantities ] = useState<Record<string, number>>( {} );


    useEffect( () => {
        if ( !itemsFetched || !inventoriesFetched ) {
            return;
        }

        if ( !itemsData?.data || !inventoryData?.data ) {
            // eslint-disable-next-line react-hooks/set-state-in-effect
            setQuantityLoading( false );
            return;
        }

        const newItemsWithQuantities: Record<string, number> = {};

        itemsData.data.forEach( item => {
            newItemsWithQuantities[ item.id ] = inventoryData.data.reduce( ( acc, inventory ) => {
                if ( inventory.item.id === item.id ) {
                    return acc + inventory.quantity;
                }
                return acc;
            }, 0 );
        } );

        setItemsWithQuantities( newItemsWithQuantities );
        setQuantityLoading( false );
    }, [ itemsFetched, inventoriesFetched, itemsData?.data, inventoryData?.data ] );

    return (
        <div className={ "flex flex-1 justify-center flex-col mt-8 items-center" }>
            {
                isLoading &&
                <div className={ "flex flex-col justify-center items-center gap-4" }>
                    <Loading text={ "Items loading..." }/>
                </div>
            }
            {
                itemsData?.data &&
                <div className={ "flex w-full items-center flex-1 flex-col gap-4" }>
                    <h1 className={ "font-bold text-2xl text-accent-foreground underline underline-offset-4" }>All
                        Items</h1>
                    <ItemsView items={ itemsData.data } quantities={ itemsWithQuantities }
                               quantityLoading={ quantityLoading }/>
                </div>
            }
        </div>

    )
}
