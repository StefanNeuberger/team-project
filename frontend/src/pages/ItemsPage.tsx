import { useGetAllItems } from "@/api/generated/items/items.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import ItemsView from "@/components/items/ItemsView.tsx";
import { useGetAllInventory } from "@/api/generated/inventory/inventory.ts";
import { useEffect, useState } from "react";

export type ItemsWithQuantitiesType = Record<string, ItemQuantityType>;

export type ItemQuantityType = {
    totalQuantity: number;
    [ warehouseName: string ]: number;
};

export default function ItemsPage() {

    const { data: itemsData, isLoading, isFetched: itemsFetched } = useGetAllItems();

    const { data: inventoryData, isFetched: inventoriesFetched } = useGetAllInventory();

    const [ quantityLoading, setQuantityLoading ] = useState<boolean>( true );

    const [ itemsWithQuantities, setItemsWithQuantities ] = useState<ItemsWithQuantitiesType>( {} );


    // Squash items and inventory to get quantities per item
    useEffect( () => {
        if ( !itemsFetched || !inventoriesFetched ) {
            return;
        }

        if ( !itemsData?.data || !inventoryData?.data ) {
            // eslint-disable-next-line react-hooks/set-state-in-effect
            setQuantityLoading( false );
            return;
        }

        // Collector for items with their quantities
        const newItemsWithQuantities: ItemsWithQuantitiesType = {};

        itemsData.data.forEach( item => {
            // Find inventory for item
            const itemInventory = inventoryData.data.filter( inventory =>
                inventory.item?.id === item?.id
            );

            // reduce inventory to get total quantity and quantities per warehouse
            newItemsWithQuantities[ item.id ] = itemInventory.reduce( ( acc, inventory ) => {

                acc.totalQuantity = ( acc.totalQuantity || 0 ) + inventory.quantity;

                acc[ inventory.warehouse.name ] = inventory.quantity;

                return acc;

            }, { totalQuantity: 0 } as { totalQuantity: number; [ key: string ]: number } );
        } );

        // set the state with squashed data
        setItemsWithQuantities( newItemsWithQuantities );
        setQuantityLoading( false );

    }, [ itemsFetched, inventoriesFetched, itemsData?.data, inventoryData?.data ] );

    return (
        <div className={ "flex flex-1 justify-center w-full flex-col mt-8 items-center" }>
            {
                isLoading &&
                <div className={ "flex flex-col justify-center items-center gap-4" }>
                    <Loading title={ "Items loading..." }/>
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
