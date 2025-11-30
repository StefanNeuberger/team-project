import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import type { ItemsViewProps } from "@/components/ItemsView.tsx";
import { motion } from "framer-motion";
import { Spinner } from "@/components/ui/spinner.tsx";
import ItemDialogDetailsView from "@/components/ItemDialogDetailsView.tsx";
import { Separator } from "@/components/ui/separator.tsx";

export default function ItemsMobileView( {
                                             items,
                                             quantities,
                                             quantityLoading
                                         }: Readonly<ItemsViewProps> ) {

    return (
        <div className={ "md:hidden flex flex-col w-full gap-4" }>
            <small>Total Items: { items?.length ?? "N/A" }</small>
            {
                items.map( ( item ) => (
                        <motion.div
                            key={ item.id }
                            initial={ { opacity: 0, x: -20 } }
                            animate={ { opacity: 1, x: 0 } }
                        >
                            <Card className={ "w-full text-center py-3 rounded-xs" }>
                                <CardHeader className={ "" }>
                                    <CardTitle className={ "text-sm" }>
                                        { item.name }
                                    </CardTitle>
                                    <CardDescription className={ "flex flex-col gap-2" }>
                                    </CardDescription>
                                    <CardContent className={ "grid text-sm text-left items-end grid-cols-3 px-0" }>
                                        <div className={ "flex relative flex-col gap-2" }>
                                            <p className={ "text-muted-foreground" }>Sku</p>
                                            <p>{ item?.sku ?? "n/a" }</p>
                                            <Separator orientation={ "vertical" } className={ "absolute right-1" }/>
                                        </div>
                                        <div className={ "flex flex-col gap-2" }>
                                            <p className={ "text-muted-foreground" }>Total #</p>
                                            <p>
                                                {
                                                    quantityLoading ?
                                                        <Spinner
                                                            className={ "mx-auto" }/> : quantities?.[ item.id ]?.totalQuantity ?? "n/a"
                                                }
                                            </p>
                                        </div>
                                        <ItemDialogDetailsView item={ item }
                                                               itemQuantity={ quantities?.[ item.id ] }/>
                                    </CardContent>
                                </CardHeader>
                            </Card>
                        </motion.div>
                    )
                )
            }
        </div>
    )
}
