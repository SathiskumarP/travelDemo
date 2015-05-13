[#-- Uses ContentModel --]

[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]

[#-- TODO Change this into a method too. --]
[#if content.referenceId?has_content]
    [#assign tourId = content.referenceId]
    [#assign tourItem = cmsfn.contentById(tourId, def.parameters.workspace)];
[#else]
    [#assign routeParameter = "tour"]
    [#assign tourItem = contentfn.getContentByParameter(routeParameter, def.parameters.workspace, "Inside-New-Delhi")!]
[/#if]
[#assign resolverConfig = {"tourTypes":"category", "destination":"category", "img":"dam"}] 
[#assign tour = contentfn.resolveReferences(tourItem, resolverConfig)]

<!-- TourDetail -->
<div class="container">

    <div class="row">
        <div class="col-sm-11 product-detail">
            <h1>${tour.name}</h1>
        </div>
        <div class="col-sm-1 product-detail">
            <a class="btn btn-primary btn-lg book-button" href="#">${i18n['tour.book']}</a>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 product-detail">

            <div class="description">${tour.description}</div>

            <ul class="product-categories list-unstyled">
            [#list tour.tourTypes as tourType]
                <li><a href="${tourType.link!}">${tourType.name!}</a></li>
            [/#list]
            </ul>

            <div class="duration">${i18n.get('tour.duration', [tour.duration!])}</div>
            <div class="locations">${tour.location!}</div>

            [#list tour.destination as destination]
                [#assign rendition = damfn.getRendition(destination.img, "small-square")!]
                <a href="${destination.@link}">
                    <div class="destination-card">
                        [@tourImage rendition "" destination.name "img-circle img-responsive" /]
                        <h3>${destination.name!}</h3>
                    </div>
                </a>
            [/#list]
        </div>

        [#assign renditionDetail = damfn.getRendition(tour.img, "large-16x9")!]
        [#if renditionDetail?has_content]
            <div class="col-md-8 product-detail">
                <span class="lead-image clearfix">
                    [@tourImage renditionDetail assetCredit tour.name "img-responsive zoomable" /]
                </span>
            </div>
        [/#if]
    </div>

    [#if tour.body?has_content]
        <div class="row">
            <div class="col-lg-12">
                <h2>${i18n['tour.details']}</h2>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 ">
                <span class="body">${tour.body!}</span>
            </div>
        </div>
    [/#if]
</div>