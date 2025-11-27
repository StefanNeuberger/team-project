import { useParams } from "react-router-dom";

export default function ShipmentsPage() {
    const { shopId } = useParams();
    return <>Shop { shopId }</>
}