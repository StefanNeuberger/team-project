import { useMemo, useState } from "react";
import type { WarehouseResponseDTO } from "@/api/generated/openAPIDefinition.schemas.ts";
import mapStyleConfig from "@/assets/map-libre-style.json";
import { type StyleSpecification } from "react-map-gl/maplibre";
import Map from 'react-map-gl/maplibre';
import 'maplibre-gl/dist/maplibre-gl.css';
import {
    FullscreenControl,
    GeolocateControl,
    Marker,
    NavigationControl,
    Popup,
    ScaleControl
} from "react-map-gl/maplibre";
import { MapPinIcon } from "lucide-react";
import { Link } from "react-router-dom";

type WarehouseMapProps = {
    warehouses: WarehouseResponseDTO[],
    shopId: string,
    centerOnFirst?: boolean,
}

export default function WarehouseMap( { warehouses, shopId, centerOnFirst }: WarehouseMapProps ) {
    const [ state, setState ] = useState<WarehouseResponseDTO | null>();
    const mapStyle = mapStyleConfig as StyleSpecification;
    const initialView = centerOnFirst ? {
        longitude: warehouses[ 0 ]!.geoLocation![ 0 ],
        latitude: warehouses[ 0 ]!.geoLocation![ 1 ],
        zoom: 8
    } : {
        latitude: 51,
        longitude: 10,
        zoom: 4
    }

    const pins = useMemo(
        () =>
            warehouses
                .filter( warehouse => warehouse.geoLocation && warehouse.geoLocation.length === 2 )
                .map( ( warehouse ) => (
                    <Marker
                        key={ `marker-${ warehouse.id }` }
                        longitude={ warehouse.geoLocation![ 0 ] }
                        latitude={ warehouse.geoLocation![ 1 ] }
                        anchor="bottom"
                        onClick={ e => {
                            e.originalEvent.stopPropagation();
                            if ( warehouse.address && warehouse.geoLocation ) {
                                setState( warehouse );
                            }
                        } }
                    >
                        <MapPinIcon className="text-red-700 size-6"></MapPinIcon>
                    </Marker>
                ) ),
        [ warehouses ]
    );

    return (
        <div
            className="relative flex w-full h-[500px] justify-center items-center rounded-xl overflow-hidden border-stone-200 border-1 outline-0">
            <Map
                initialViewState={ initialView }
                mapStyle={ mapStyle }
            >
                <GeolocateControl position="top-left"/>
                <FullscreenControl position="top-left"/>
                <NavigationControl position="top-left"/>
                <ScaleControl/>

                { pins }

                { state && (
                    <Popup
                        anchor="top"
                        longitude={ Number( state.geoLocation![ 0 ] ) }
                        latitude={ Number( state.geoLocation![ 1 ] ) }
                        onClose={ () => setState( null ) }
                        className="**:[&_button]:outline-0 **:[&_button]:text-lg **:[&_button]:mr-0 **:[&_button]:px-2 **:[&_button]:pb-1 **:[&_button]:rounded-none! [&_maplibregl-popup-content]:rounded-lg [&_maplibregl-popup-content]:overflow-hidden"
                    >
                        <div className="pr-6">
                            <Link className={ "outline-none hover:underline" }
                                  to={ `/shop/${ shopId }/warehouses/${ state.id }` }><strong>{ state.name }</strong><br/></Link>
                            { state.address?.street } { state.address?.number }<br/>
                            { state.address?.postalCode } { state.address?.city }<br/>
                            { state.address?.state }<br/>
                            { state.address?.country }
                        </div>

                    </Popup>
                ) }

            </Map>
        </div>
    );
}