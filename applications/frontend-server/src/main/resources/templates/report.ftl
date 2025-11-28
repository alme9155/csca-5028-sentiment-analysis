<#import "template.ftl" as t>

<@t.noauthentication title="Top Movie Ratings Report">
<div class="container result-page">
    <h1>Top Movie Ratings Report</h1>
    <div class="top-movies-table">
        <h3>(Sorted by Review Sentiment Analysis Rating)</h3>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Movie Title</th>
                    <th>Average Rating</th>
                </tr>
            </thead>
            <tbody>
            <#-- NOTE: topMovies is the list passed from Ktor -->
            <#list topMovies as movie>
                <tr>
                    <td>${movie?index + 1}</td>
                    <td>${movie.title}</td>
                    <td>${movie.score?string("0.##")}/5</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="probabilities">
        <h3>Ratings Chart</h3>
        <div class="bars">
            <#list topMovies as movie>
                <div class="bar-container">
                    <span class="label-text">${movie.title}</span>
                    <div class="bar-wrapper">
                        <div class="bar positive"
                             style="width: ${(movie.scorePercent)?string("0.##")}%">
                            <span class="percentage">
                                ${movie.score?string("0.##")}/5
                            </span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>

    <div class="back">
        <a href="/" class="btn">Submit Another Movie Review</a>
    </div>
</div>
</@t.noauthentication>
