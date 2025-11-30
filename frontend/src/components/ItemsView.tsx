import { type Item } from '../api/generated/openAPIDefinition.schemas';
import ItemsSearchBar from "@/components/ItemsSearchBar.tsx";
import { useState } from "react";
import ItemsDefaultView from "@/components/ItemsDefaultView.tsx";
import ItemsMobileView from "@/components/ItemsMobileView.tsx";
import type { ItemsWithQuantitiesType } from "@/pages/ItemsPage.tsx";

export type ItemsViewProps = {
    items: ItemViewItem[],
    quantities: ItemsWithQuantitiesType,
    quantityLoading?: boolean,
}

export type ItemViewItem = Item & {
    quantity?: number;

}
export default function ItemsView( { items, quantities, quantityLoading }: Readonly<ItemsViewProps> ) {

    const [ searchQuery, setSearchQuery ] = useState<string>( "" );

    const [ filteredItems, setFilteredItems ] = useState<Item[] | []>( items || [] );

    const handleSearchInputChange = ( query: string ) => {
        setSearchQuery( query );

        setFilteredItems( items.filter( ( item ) => {
            const lowerCaseQuery = query.toLowerCase();
            const skuCases = item.sku ? [ item.sku.toLowerCase(), item.sku.replaceAll( "-", " " ).toLowerCase(), item.sku.replaceAll( "-", "" ).toLowerCase() ] : [];

            return item.name.toLowerCase().includes( lowerCaseQuery ) || skuCases.some( sku => sku.includes( lowerCaseQuery ) );
        } ) );
    };

    return (
        <div className={ "w-full flex flex-col px-8 mb-8" }>
            <ItemsSearchBar onInputChange={ handleSearchInputChange } inputValue={ searchQuery }/>
            {/* Item View <= 768 px (MOBILE)*/ }
            <ItemsMobileView items={ filteredItems } quantities={ quantities } quantityLoading={ quantityLoading }/>
            {/* Item View >= 768 px (TABLET / DESKTOP) */ }
            <ItemsDefaultView items={ filteredItems } quantities={ quantities }
                              quantityLoading={ quantityLoading }/>
            { filteredItems?.length === 0 &&
                <p className={ "m-auto my-8 text-accent-foreground" }>No Items found</p>
            }
        </div>
    )
}
